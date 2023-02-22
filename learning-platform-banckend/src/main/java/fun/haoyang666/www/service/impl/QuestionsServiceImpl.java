package fun.haoyang666.www.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.QuesEnum;
import fun.haoyang666.www.domain.dto.PKRecordDTO;
import fun.haoyang666.www.domain.entity.Quesrecord;
import fun.haoyang666.www.domain.entity.Records;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.domain.vo.*;
import fun.haoyang666.www.domain.entity.Questions;
import fun.haoyang666.www.domain.req.GetAnswerREQ;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.mapper.QuesrecordMapper;
import fun.haoyang666.www.service.QuesrecordService;
import fun.haoyang666.www.service.QuestionsService;
import fun.haoyang666.www.mapper.QuestionsMapper;
import fun.haoyang666.www.service.RecordsService;
import fun.haoyang666.www.service.UserService;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yang
 * @description 针对表【questions(题目)】的数据库操作Service实现
 * @createDate 2022-12-10 16:20:50
 */
@Service
@Slf4j
public class QuestionsServiceImpl extends ServiceImpl<QuestionsMapper, Questions>
        implements QuestionsService {

    @Resource
    private QuestionsMapper questionsMapper;
    @Resource
    private QuesrecordService quesrecordService;
    @Resource
    private QuesrecordMapper quesrecordMapper;
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, LeaderVO> redisTemplate;
    @Resource
    private RecordsService recordsService;


    @Override
    public Map<Integer, List<QuesVO>> getQuestions(long userId, long sum, String source, String difficult) {
        if (sum <= Constant.MIN_NUM || sum > Constant.MAX_NUM) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目数量不合法");
        }
        QuesEnum quesEnum = QuesEnum.getQuesEnum(source);
        switch (quesEnum) {
            case RANDOM_QUES:
                return randomQues(sum);
            case MISTAKE_QUES:
                return mistakeQues(userId, sum);
            case MIS_QUES:
                return randomQues(sum);
            case NEW_QUES:
                return randomQues(sum);
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

    }

    @Override
    public Map<Integer, List<QuesVO>> randomQues(long sum) {
        List<QuesVO> quesVOS = questionsMapper.selectRandom(sum);
        return quesVOS.stream().collect(Collectors.groupingBy(QuesVO::getType));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void judgeGrade(GetAnswerREQ getAnswerReq) {
        List<Long> quesIds = getAnswerReq.getQuesIds();
        Long userId = ThreadLocalUtils.get();
        Long time = getAnswerReq.getTime();
        Long opponent = getAnswerReq.getOpponent();
        Boolean result = getAnswerReq.getResult();
        Map<Long, String> answer = getAnswerReq.getAnswer();
        CorrectVO vo = beforeJudge(quesIds, answer, getAnswerReq.getTime());
        List<GradeVO> correctList = vo.getCorrect();
        List<GradeVO> failure = vo.getFailure();
        int correctCount = correctList.size();
        int falseCount = failure.size();
        //形成做题记录
        long recordId = recordsService.saveRecord(userId, time, correctCount + falseCount, correctCount, opponent, result, null);
        //记录日榜
        dayLeader(userId, correctCount);
        //修改用户正确数,写入数据库
        //存储做题记录
        quesrecordService.saveRecordQues(recordId, userId, correctList, failure);
        userService.updateScore(userId, correctCount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void PKGrade(PKResultVO pkResultVO) {
        List<GradeVO> correctList = pkResultVO.getCorrectList();
        List<GradeVO> failureList = pkResultVO.getFailureList();
        int correctCount = pkResultVO.getCorrectList().size();
        int falseCount = pkResultVO.getFailureList().size();
        Long userId = Long.parseLong(pkResultVO.getUserId());
        Long time = pkResultVO.getTime();
        Long opponent = Long.parseLong(pkResultVO.getOpponent());
        Boolean result = pkResultVO.getResult();
        //形成做题记录
        long recordId = recordsService.saveRecord(userId, time, correctCount + falseCount, correctCount, opponent, result, pkResultVO.getPkId());
        //记录日榜
        dayLeader(userId, correctCount);
        //修改用户正确数,写入数据库
        log.info("user:{}", userId);
        //存储做题记录
        quesrecordService.saveRecordQues(recordId, userId, correctList, failureList);
        userService.updateScore(userId, correctCount);
    }

    @Override
    public Map<Integer, List<QuesVO>> getQuesRandom() {
        return randomQues(20);
    }

    @Override
    public CorrectVO beforeJudge(List<Long> quesIds, Map<Long, String> answer, Long time) {
        //查询所有题目
        List<Questions> ques = this.listByIds(quesIds);
        //问卷判别
        List<GradeVO> gradeVOs = convertToDto(ques, answer);
        //正确错误分类
        Map<Boolean, List<GradeVO>> collect = gradeVOs.stream().collect(Collectors.groupingBy(GradeVO::isCorrect));
        //正确总数
        List<GradeVO> correctList = Optional.ofNullable(collect.get(true)).orElse(new ArrayList<>());
        log.info("true:{}", correctList);
        //错误总数
        List<GradeVO> falseList = Optional.ofNullable(collect.get(false)).orElse(new ArrayList<>());
        CorrectVO vo = new CorrectVO();
        vo.setCorrect(correctList);
        vo.setFailure(falseList);
        vo.setTime(time);
        return vo;
    }

    @Override
    public PKRecordDTO recordDetail(Long recordId) {
        PKRecordDTO dto = new PKRecordDTO();
        List<Quesrecord> list = quesrecordService.lambdaQuery().eq(Quesrecord::getRecordId, recordId).list();
        HashMap<Long, String> recordMap = new HashMap<>();
        for (Quesrecord quesrecord : list) {
            recordMap.put(quesrecord.getQuestionId(), quesrecord.getUserAnswer());
        }
        Set<Long> quesIds = recordMap.keySet();
        List<Questions> questions = this.listByIds(quesIds);
        ArrayList<RecordDetailVO> recordDetailVOS = new ArrayList<>();
        for (Questions question : questions) {
            RecordDetailVO recordDetailVO = new RecordDetailVO();
            BeanUtils.copyProperties(question, recordDetailVO);
            recordDetailVO.setUserAnswer(recordMap.get(question.getId()));
            recordDetailVOS.add(recordDetailVO);
        }

        for (RecordDetailVO recordDetailVO : recordDetailVOS) {
            if (recordDetailVO.getType() == 0) {
                dto.getRadio().add(recordDetailVO);
            } else {
                dto.getMulti().add(recordDetailVO);
            }
        }
        return dto;
    }

    @Override
    public PKRecordDTO getPkDetails(Long recordId) {
        //题目信息
        PKRecordDTO pkRecordDTO = this.recordDetail(recordId);
        //对手记录
        Records records = recordsService.getById(recordId);
        Long opponent = records.getOpponent();
        User user = userService.getById(opponent);
        pkRecordDTO.setOpponentUrl(user.getAvatarUrl());
        pkRecordDTO.setOpponentName(user.getUsername());
        String pkId = records.getPkId();
        Records opponentRecord = recordsService.lambdaQuery()
                .eq(Records::getUserId, opponent)
                .eq(Records::getPkId, pkId)
                .one();
        pkRecordDTO.setCorrect(opponentRecord.getCurrectSum());
        LocalDateTime startTime = opponentRecord.getStartTime();
        LocalDateTime endTime = opponentRecord.getEndTime();
        long seconds = Duration.between(startTime, endTime).getSeconds();
        pkRecordDTO.setTime(seconds);
        pkRecordDTO.setResult(records.getResult() == 1);
        return pkRecordDTO;
    }


    private void dayLeader(long userId, long count) {
        ZSetOperations<String, LeaderVO> zSet = redisTemplate.opsForZSet();
        User user = userService.lambdaQuery().eq(User::getId, userId).one();
        LeaderVO day = new LeaderVO();
        String username = user.getUsername();
        day.setId(userId);
        day.setUsername(username);
        Double score = Optional.ofNullable(zSet.score(Constant.REDIS_DAY_LEADER, day)).orElse((double) 0);
        log.info("--->score:{}", score);
        score += count;
        log.info("score:{}", score);
        zSet.add(Constant.REDIS_DAY_LEADER, day, score);
    }

    private List<GradeVO> convertToDto(List<Questions> questions, Map<Long, String> userMap) {
        LinkedList<GradeVO> list = new LinkedList<>();
        for (Questions question : questions) {
            GradeVO dto = new GradeVO();
            dto.setQuesId(question.getId());
            dto.setCorrectAnswer(question.getCorrect());
            String userAnswer = userMap.get(question.getId());
            dto.setUserAnswer(userAnswer);
            dto.setCorrect(question.getCorrect().equalsIgnoreCase(userAnswer));
            log.info("dto:{}", dto);
            list.add(dto);
        }
        return list;
    }

    private Map<Integer, List<QuesVO>> mistakeQues(long userId, long sum) {
        List<Long> ids = quesrecordMapper.selectIdsByUserIdMistack(userId);
        Map<Integer, List<QuesVO>> map = new HashMap<>();
        if (ids.size() <= sum) {
            map = randomQues(sum);
        } else {
            List<Long> randomIds = RandomUtil.randomEleList(ids, (int) sum);
            map = this.listByIds(randomIds).stream().map(this::convertVo).collect(Collectors.groupingBy(QuesVO::getType));
        }
        return map;
    }

    private QuesVO convertVo(Questions questions) {
        QuesVO quesVo = new QuesVO();
        log.info("ques:{}", questions);
        BeanUtils.copyProperties(questions, quesVo);
        return quesVo;
    }
}




