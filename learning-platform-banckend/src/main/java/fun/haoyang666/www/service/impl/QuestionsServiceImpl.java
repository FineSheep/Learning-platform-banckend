package fun.haoyang666.www.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.common.ErrorCode;
import fun.haoyang666.www.domain.Questions;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.listener.QuestionListener;
import fun.haoyang666.www.service.QuestionsService;
import fun.haoyang666.www.mapper.QuestionsMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author yang
 * @description 针对表【questions(题目)】的数据库操作Service实现
 * @createDate 2022-12-10 16:20:50
 */
@Service
public class QuestionsServiceImpl extends ServiceImpl<QuestionsMapper, Questions>
        implements QuestionsService {

    @Resource
    private QuestionsMapper questionsMapper;


    @Override
    public List<Questions> getQuesRandom(int size) {
        List<Questions> questions = null;
        try {
            questions = questionsMapper.selectRadon(size);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return questions;
    }


}




