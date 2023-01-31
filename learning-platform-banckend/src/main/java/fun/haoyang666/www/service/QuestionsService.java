package fun.haoyang666.www.service;

import fun.haoyang666.www.domain.vo.GradeVO;
import fun.haoyang666.www.domain.entity.Questions;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.haoyang666.www.domain.req.GetAnswerREQ;
import fun.haoyang666.www.domain.vo.QuesVO;

import java.util.List;
import java.util.Map;

/**
* @author yang
* @description 针对表【questions(题目)】的数据库操作Service
* @createDate 2022-12-10 16:20:50
*/
public interface QuestionsService extends IService<Questions> {
    Map<Integer, List<QuesVO>> getQuestions(long userId, long sum, String source, String difficult);

    Map<Integer, List<QuesVO>> randomQues(long sum);

    List<GradeVO> judgeGrade(GetAnswerREQ getAnswerReq);

    Map<Integer, List<QuesVO>> getQuesRandom();



//    Map<Integer, List<Questions>> getQuesRandom(int size);


}
