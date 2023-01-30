package fun.haoyang666.www.mapper;

import fun.haoyang666.www.domain.entity.Questions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.haoyang666.www.domain.vo.QuesVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yang
 * @description 针对表【questions(题目)】的数据库操作Mapper
 * @createDate 2022-12-10 16:20:50
 * @Entity com.haoyang666.fun.domain.Questions
 */
public interface QuestionsMapper extends BaseMapper<Questions> {


/*    @Select("select distinct * from questions  order by rand() limit #{size}")
    List<Questions> selectRandom(int size);*/

    List<QuesVo> selectRandom(long sum);


}




