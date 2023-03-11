package fun.haoyang666.www.admin.controller;

import com.alibaba.excel.EasyExcel;
import fun.haoyang666.www.admin.dto.ListQuesDto;
import fun.haoyang666.www.annotation.CheckAuth;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.entity.Questions;
import fun.haoyang666.www.listener.QuestionListener;
import fun.haoyang666.www.service.QuestionsService;
import fun.haoyang666.www.utils.ResultUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author yang
 * @createTime 2023/3/9 11:22
 * @description
 */
@RequestMapping("sysQues")
@RestController
@CheckAuth("admin")
public class SysQuestionController {

    @Resource
    private QuestionsService questionsService;

    @PostMapping("listQuestion")
    public BaseResponse listQuestion(@RequestBody ListQuesDto dto){
        return ResultUtils.success(questionsService.listQuestion(dto));
    }

    @PostMapping("saveOrUpdateQues")
    public BaseResponse saveOrUpdate(@RequestBody Questions questions){
        return ResultUtils.success(questionsService.saveOrUpdateQues(questions));
    }

    @PostMapping("uploadQues")
    public BaseResponse uploadQues(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), Questions.class, new QuestionListener(questionsService)).sheet().doRead();
        } catch (IOException e) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(null);
    }

    @GetMapping("removeQuestion")
    public BaseResponse removeQuestion(Long id){
        return ResultUtils.success(questionsService.removeById(id));
    }
}
