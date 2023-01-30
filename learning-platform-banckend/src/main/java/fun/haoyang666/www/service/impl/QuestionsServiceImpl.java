package fun.haoyang666.www.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.QuesEnum;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.domain.vo.GradeVo;
import fun.haoyang666.www.domain.entity.Quesrecord;
import fun.haoyang666.www.domain.entity.Questions;
import fun.haoyang666.www.domain.req.GetAnswerReq;
import fun.haoyang666.www.domain.vo.LeaderVo;
import fun.haoyang666.www.domain.vo.QuesVo;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.mapper.QuesrecordMapper;
import fun.haoyang666.www.service.QuesrecordService;
import fun.haoyang666.www.service.QuestionsService;
import fun.haoyang666.www.mapper.QuestionsMapper;
import fun.haoyang666.www.service.RecordsService;
import fun.haoyang666.www.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private RedisTemplate<String, LeaderVo> redisTemplate;
    @Resource
    private RecordsService recordsService;


    @Override
    public Map<Integer, List<QuesVo>> getQuestions(long userId, long sum, String source, String difficult) {
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
    public Map<Integer, List<QuesVo>> randomQues(long sum) {
        List<QuesVo> quesVos = questionsMapper.selectRandom(sum);
        return quesVos.stream().collect(Collectors.groupingBy(QuesVo::getType));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public List<GradeVo> judgeGrade(GetAnswerReq getAnswerReq) {
        List<Long> quesIds = getAnswerReq.getQuesIds();
        Long userId = getAnswerReq.getUserId();
        Long time = getAnswerReq.getTime();
        Map<Long, String> answer = getAnswerReq.getAnswer();
        //查询所有题目
        List<Questions> ques = this.listByIds(quesIds);
        //问卷判别
        List<GradeVo> gradeVos = convertToDto(ques, answer);
        //正确错误分类
        Map<Boolean, List<GradeVo>> collect = gradeVos.stream().collect(Collectors.groupingBy(GradeVo::isCorrect));
        //正确总数
        List<GradeVo> correctList = Optional.ofNullable(collect.get(true)).orElse(new ArrayList<>());
        log.info("true:{}", correctList);
        long correctCount = correctList.size();
        //错误总数
        List<GradeVo> falseList = Optional.ofNullable(collect.get(false)).orElse(new ArrayList<>());
        long falseCount = falseList.size();
        //形成做题记录
        long recordId = recordsService.saveRecord(userId, time, correctCount + falseCount, correctCount);
        //记录日榜
        dayLeader(getAnswerReq.getUserId(), correctCount);
        //修改用户正确数,写入数据库
        log.info("user:{}", getAnswerReq.getUserId());
        //存储做题记录
        quesrecordService.saveRecordQues(recordId, userId, correctList, falseList);
        userService.updateScore(getAnswerReq.getUserId(), correctCount);
        return gradeVos;
    }

    @Override
    public Map<Integer, List<QuesVo>> getQuesRandom() {
        return randomQues(20);
    }

    private void dayLeader(long userId, long count) {
        ZSetOperations<String, LeaderVo> zSet = redisTemplate.opsForZSet();
        User user = userService.lambdaQuery().eq(User::getId, userId).one();
        LeaderVo day = new LeaderVo();
        String username = user.getUsername();
        day.setId(userId);
        day.setUsername(username);
        Double score = Optional.ofNullable(zSet.score(Constant.REDIS_DAY_LEADER, day)).orElse((double) 0);
        log.info("--->score:{}", score);
        score += count;
        log.info("score:{}", score);
        zSet.add(Constant.REDIS_DAY_LEADER, day, score);
    }

    private List<GradeVo> convertToDto(List<Questions> questions, Map<Long, String> userMap) {
        LinkedList<GradeVo> list = new LinkedList<>();
        for (Questions question : questions) {
            GradeVo dto = new GradeVo();
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

    private Map<Integer, List<QuesVo>> mistakeQues(long userId, long sum) {
        List<Long> ids = quesrecordMapper.selectIdsByUserIdMistack(userId);
        Map<Integer, List<QuesVo>> map = new HashMap<>();
        if (ids.size() <= sum) {
            map = randomQues(sum);
        } else {
            List<Long> randomIds = RandomUtil.randomEleList(ids, (int) sum);
            map = this.listByIds(randomIds).stream().map(this::convertVo).collect(Collectors.groupingBy(QuesVo::getType));
        }
        return map;
    }

    private QuesVo convertVo(Questions questions) {
        QuesVo quesVo = new QuesVo();
        log.info("ques:{}", questions);
        BeanUtils.copyProperties(questions, quesVo);
        return quesVo;
    }
}




