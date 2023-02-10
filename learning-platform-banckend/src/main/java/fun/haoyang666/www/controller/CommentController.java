package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.service.CommentService;
import fun.haoyang666.www.utils.ResultUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @PostMapping("addComment")
    public BaseResponse addComment() {
        return ResultUtils.success("");
    }
}
