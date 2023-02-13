package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.SuccessCode;
import fun.haoyang666.www.domain.dto.ScrollerDTO;
import fun.haoyang666.www.domain.entity.Post;
import fun.haoyang666.www.domain.req.GetPostREQ;
import fun.haoyang666.www.domain.req.SavePostREQ;
import fun.haoyang666.www.domain.vo.PostVO;
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
    public BaseResponse savePost(@RequestBody SavePostREQ postReq) {
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
    public BaseResponse<PostVO> getPost(Long postId, Long userId) {
//        Post post = postService.lambdaQuery().eq(Post::getId, postId).one();
        PostVO vo = postService.getPost(postId, userId);
        return ResultUtils.success(vo);
    }

    @GetMapping("getPosts")
    public BaseResponse<ScrollerDTO<PostVO>> getPosts(GetPostREQ getPostReq) {
        if (getPostReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        int curPage = getPostReq.getCurPage();
        int pageSize = getPostReq.getPageSize();
        Long userId = getPostReq.getUserId();
        ScrollerDTO<PostVO> posts = postService.getPosts(userId, curPage, pageSize);
        return ResultUtils.success(posts);
    }

    @GetMapping("getPostByUserId")
    public BaseResponse getPostByUserId(GetPostREQ getPostReq) {
        int curPage = getPostReq.getCurPage();
        int pageSize = getPostReq.getPageSize();
        Long userId = getPostReq.getUserId();
        List<PostVO> vos = postService.getPostUid(curPage, pageSize, userId);
        return ResultUtils.success(vos);
    }

    @GetMapping("deletePost")
    public BaseResponse deletePost(Long postId) {
        return ResultUtils.success(postService.removeById(postId));
    }

}
