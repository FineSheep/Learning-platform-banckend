package fun.haoyang666.www.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.QuesEnum;
import fun.haoyang666.www.domain.dto.GradeDto;
import fun.haoyang666.www.domain.entity.Quesrecord;
import fun.haoyang666.www.domain.entity.Questions;
import fun.haoyang666.www.domain.req.GetAnswerReq;
import fun.haoyang666.www.domain.vo.QuesVo;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.QuesrecordService;
import fun.haoyang666.www.service.QuestionsService;
import fun.haoyang666.www.mapper.QuestionsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
    public List<GradeDto> judgeGrade(GetAnswerReq getAnswerReq) {
        List<Long> quesIds = getAnswerReq.getQuesIds();
        List<Questions> ques = this.listByIds(quesIds);
        List<GradeDto> gradeDtos = convertToDto(ques, getAnswerReq.getAnswer());

        return gradeDtos;
    }

    private List<GradeDto> convertToDto(List<Questions> questions, Map<Long, String> userMap) {

        LinkedList<GradeDto> list = new LinkedList<>();
        for (Questions question : questions) {
            GradeDto dto = new GradeDto();
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
        List<Quesrecord> records = quesrecordService.lambdaQuery()
                .eq(Quesrecord::getUserId, userId).eq(Quesrecord::getIsCorrect, 0).list();
        Map<Integer, List<QuesVo>> map = new HashMap<>();
        if (records.size() <= sum) {
            map = randomQues(sum);
        } else {
            List<Long> ids = records.stream().map(Quesrecord::getId).collect(Collectors.toList());
            List<Long> randomIds = RandomUtil.randomEleList(ids, (int) sum);
            map = this.listByIds(randomIds).stream().map(this::convertVo).collect(Collectors.groupingBy(QuesVo::getType));
        }
        return map;
    }


    private QuesVo convertVo(Questions questions) {
        QuesVo quesVo = new QuesVo();
        BeanUtils.copyProperties(questions, quesVo);

        return quesVo;
    }
}




