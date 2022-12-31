package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.entity.Questions;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.QuestionsService;
import fun.haoyang666.www.mapper.QuestionsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Map<Integer, List<Questions>> getQuesRandom(int size) {
        Map<Integer, List<Questions>> collects = null;
        try {
            List<Questions> questions = questionsMapper.selectRandom(size);
            collects = questions.stream().collect(Collectors.groupingBy(Questions::getType));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return collects;
    }


}




