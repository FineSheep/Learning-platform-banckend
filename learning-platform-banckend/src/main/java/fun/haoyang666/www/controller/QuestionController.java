package fun.haoyang666.www.controller;

import com.alibaba.excel.EasyExcel;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.ResultUtils;
import fun.haoyang666.www.domain.entity.Questions;
import fun.haoyang666.www.domain.req.GetQuesReq;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.listener.QuestionListener;
import fun.haoyang666.www.service.QuestionsService;

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

/*    @GetMapping("getQues")
    public BaseResponse getQues(int num) {
        if (num <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Map<Integer, List<Questions>> ques = questionsService.getQuesRandom(num);
        return ResultUtils.success(ques);
    }*/

    @PostMapping("uploadQues")
    public BaseResponse uploadQues(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), Questions.class, new QuestionListener(questionsService)).sheet().doRead();
        } catch (IOException e) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(null);
    }

    @PostMapping("getQuesBy")
    public BaseResponse getQuesBy(@RequestBody GetQuesReq quesReq) {
        if (quesReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long sum = quesReq.getSum();
        String difficulty = quesReq.getDifficulty();
        String source = quesReq.getSource();
        long userId = quesReq.getUserId();
        return null;
    }
}
