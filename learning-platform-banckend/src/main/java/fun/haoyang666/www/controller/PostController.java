package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.SuccessCode;
import fun.haoyang666.www.domain.dto.ScrollerDto;
import fun.haoyang666.www.domain.entity.Post;
import fun.haoyang666.www.domain.req.GetPostReq;
import fun.haoyang666.www.domain.req.SavePostReq;
import fun.haoyang666.www.domain.vo.PostVo;
import fun.haoyang666.www.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yang
 * @createTime 2023/1/14 15:03
 * @description
 */
@RestController
@RequestMapping("post")
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("savePost")
    public BaseResponse savePost(@RequestBody SavePostReq postReq) {
        if (postReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String content = postReq.getContent();
        String title = postReq.getTitle();
        String description = postReq.getDescription();
        String photo = postReq.getPhoto();
        long userId = postReq.getUserId();
        List<Long> tags = postReq.getTags();
        if (StringUtils.isAnyBlank(content, title, description)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        postService.savePost(content, title, description, photo, userId, tags);
        return ResultUtils.success(SuccessCode.SUCCESS);
    }

    @GetMapping("getPost")
    public BaseResponse<Post> getPost(long postId) {
        Post post = postService.lambdaQuery().eq(Post::getId, postId).one();
        return ResultUtils.success(post);
    }

    @GetMapping("getPosts")
    public BaseResponse<ScrollerDto<PostVo>> getPosts(GetPostReq getPostReq) {
        if (getPostReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        int curPage = getPostReq.getCurPage();
        int pageSize = getPostReq.getPageSize();
        Long userId = getPostReq.getUserId();
        ScrollerDto<PostVo> posts = postService.getPosts(userId, curPage, pageSize);
        return ResultUtils.success(posts);
    }
}
