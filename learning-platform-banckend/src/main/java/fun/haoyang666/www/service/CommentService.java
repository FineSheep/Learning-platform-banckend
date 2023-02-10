package fun.haoyang666.www.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.haoyang666.www.domain.entity.Comment;
import fun.haoyang666.www.domain.vo.CommentVo;

import java.util.List;

public interface CommentService extends IService<Comment> {


    void addComment(Comment comment);

    List<CommentVo> getAllComments(Long postId);
}
