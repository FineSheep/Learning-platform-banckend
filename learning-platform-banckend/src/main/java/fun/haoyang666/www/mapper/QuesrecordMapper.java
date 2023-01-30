package fun.haoyang666.www.mapper;

import fun.haoyang666.www.domain.entity.Quesrecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author yang
* @description 针对表【quesrecord(题目记录关联)】的数据库操作Mapper
* @createDate 2023-01-07 14:00:56
* @Entity fun.haoyang666.www.domain.entity.Quesrecord
*/
public interface QuesrecordMapper extends BaseMapper<Quesrecord> {


    List<Long> selectIdsByUserIdMistack(Long userId);
}




