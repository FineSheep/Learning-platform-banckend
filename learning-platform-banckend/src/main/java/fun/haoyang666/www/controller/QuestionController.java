package fun.haoyang666.www.controller;

import com.alibaba.excel.EasyExcel;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.vo.GradeVO;
import fun.haoyang666.www.domain.req.GetAnswerREQ;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.domain.entity.Questions;
import fun.haoyang666.www.domain.req.GetQuesREQ;
import fun.haoyang666.www.domain.vo.QuesVO;
import fun.haoyang666.www.listener.QuestionListener;
import fun.haoyang666.www.service.QuestionsService;

import fun.haoyang666.www.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yang
 * @createTime 2022/12/11 18:18
 * @description
 */
@RestController
@RequestMapping("question")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionsService questionsService;




    @PostMapping("getQuesBy")
    public BaseResponse<Map<Integer, List<QuesVO>>> getQuesBy(@RequestBody GetQuesREQ quesReq) {
        if (quesReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long sum = quesReq.getSum();
        String difficulty = quesReq.getDifficulty();
        String source = quesReq.getSource();
        Long userId = ThreadLocalUtils.get().getUserId();
        Map<Integer, List<QuesVO>> questions = questionsService.getQuestions(userId, sum, source, difficulty);
        return ResultUtils.success(questions);
    }

    @PostMapping("putAnswer")
    public BaseResponse putAnswer(@RequestBody GetAnswerREQ getAnswerReq) {
        log.info("req:{}", getAnswerReq);
        questionsService.judgeGrade(getAnswerReq);
        return ResultUtils.success(Constant.SUCCESS);
    }

    @GetMapping("getQuesRandom")
    public BaseResponse getQuesRandom() {
        Map<Integer, List<QuesVO>> questions = questionsService.getQuesRandom();
        return ResultUtils.success(questions);
    }
    @GetMapping("recordDetail")
    public BaseResponse recordDetail(Long recordId) {
        return ResultUtils.success(questionsService.recordDetail(recordId));
    }

    @GetMapping("getPkDetails")
    public BaseResponse getPkDetails(Long recordId){
        return ResultUtils.success(questionsService.getPkDetails(recordId));
    }
}
