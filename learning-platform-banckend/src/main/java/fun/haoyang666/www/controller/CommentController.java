package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.entity.Comment;
import fun.haoyang666.www.domain.vo.CommentVo;
import fun.haoyang666.www.service.CommentService;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/2/10 15:03
 * @description
 */
@RestController
@RequestMapping("comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @GetMapping("getAllComments")
    public BaseResponse<List<CommentVo>> getAllComments(Long postId) {
        List<CommentVo> lists = commentService.getAllComments(postId);
        return ResultUtils.success(lists);
    }

    @PostMapping("addComment")
    public BaseResponse addComment(@RequestBody Comment comment) {
        String id = comment.getId();
        String content = comment.getContent();
        Long postId = comment.getPostId();
        Long userId = ThreadLocalUtils.get().getUserId();
        comment.setUserId(userId);
        if (StringUtils.isAnyBlank(id, content, Long.toString(postId), Long.toString(userId))) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        commentService.addComment(comment);
        return ResultUtils.success(Constant.SUCCESS);
    }
}
