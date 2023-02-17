package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.common.enums.SysMessageEnum;
import fun.haoyang666.www.domain.entity.Comment;
import fun.haoyang666.www.domain.entity.Message;
import fun.haoyang666.www.domain.entity.MessageUser;
import fun.haoyang666.www.domain.entity.Post;
import fun.haoyang666.www.domain.vo.CommentVo;
import fun.haoyang666.www.mapper.CommentMapper;
import fun.haoyang666.www.mapper.PostMapper;
import fun.haoyang666.www.service.CommentService;
import fun.haoyang666.www.service.MessageService;
import fun.haoyang666.www.service.MessageUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private PostMapper postMapper;
    @Resource
    private MessageService messageService;


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addComment(Comment comment) {
        this.save(comment);
        Long postId = comment.getPostId();
        String parentId = comment.getParentId();
        if (parentId == null) {
            //回复主贴
            messageService.commentMessage(comment, null);
        } else {
            //回复某个人
            Comment one = this.lambdaQuery().eq(Comment::getId, parentId).one();
            Long userId = one.getUserId();
            messageService.commentMessage(comment, userId);
        }
        postMapper.updatePostComment(postId);
    }

    @Override
    public List<CommentVo> getAllComments(Long postId) {
        List<CommentVo> comments = commentMapper.getComments(postId);
        return comments;
    }
}
