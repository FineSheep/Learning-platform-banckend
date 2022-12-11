package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.Questions;
import fun.haoyang666.www.service.QuestionsService;
import fun.haoyang666.www.mapper.QuestionsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        List<Questions> questions = questionsMapper.selectRadon(size);
        return questions;
    }

    public String uploadExcel(){
        return "";
    }
}




