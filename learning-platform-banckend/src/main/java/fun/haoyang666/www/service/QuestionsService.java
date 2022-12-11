package fun.haoyang666.www.service;

import fun.haoyang666.www.domain.Questions;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author yang
* @description 针对表【questions(题目)】的数据库操作Service
* @createDate 2022-12-10 16:20:50
*/
public interface QuestionsService extends IService<Questions> {


    List<Questions> getQuesRandom(int size);


}
