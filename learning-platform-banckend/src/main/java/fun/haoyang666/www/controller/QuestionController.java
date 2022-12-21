package fun.haoyang666.www.controller;

import com.alibaba.excel.EasyExcel;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.ResultUtils;
import fun.haoyang666.www.domain.entity.Questions;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.listener.QuestionListener;
import fun.haoyang666.www.service.QuestionsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
public class QuestionController {

    @Resource
    private QuestionsService questionsService;

    @GetMapping("getQues")
    public BaseResponse getQues(int num) {
        if (num <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Map<Integer, List<Questions>> ques = questionsService.getQuesRandom(num);
        return ResultUtils.success(ques);
    }

    @PostMapping("uploadQues")
    public BaseResponse uploadQues(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), Questions.class, new QuestionListener(questionsService)).sheet().doRead();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(null);
    }
}
