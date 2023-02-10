package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.entity.Comment;
import fun.haoyang666.www.domain.vo.CommentVo;
import fun.haoyang666.www.mapper.CommentMapper;
import fun.haoyang666.www.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/2/10 15:02
 * @description
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public void addComment(Comment comment) {
        this.save(comment);
    }

    @Override
    public List<CommentVo> getAllComments(Long postId) {
        List<CommentVo> comments = commentMapper.getComments(postId);
        for (CommentVo comment : comments) {
            log.info("comment:----》{}", comment);
        }
        return comments;
    }
}
