package fun.haoyang666.www.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.haoyang666.www.domain.entity.Comment;
import fun.haoyang666.www.domain.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentVo> getComments(Long postId);
}
