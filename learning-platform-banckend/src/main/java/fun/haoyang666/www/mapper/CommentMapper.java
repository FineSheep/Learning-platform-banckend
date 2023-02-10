package fun.haoyang666.www.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.haoyang666.www.domain.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
