package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.SuccessCode;
import fun.haoyang666.www.domain.req.GetPostActionsREQ;
import fun.haoyang666.www.domain.req.PageREQ;
import fun.haoyang666.www.domain.req.SavePostREQ;
import fun.haoyang666.www.domain.vo.PostVO;
import fun.haoyang666.www.service.PostService;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        Long userId = ThreadLocalUtils.get();
        List<Long> tags = postReq.getTags();
        if (StringUtils.isAnyBlank(content, title, description)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        postService.savePost(content, title, description, photo, userId, tags);
        return ResultUtils.success(SuccessCode.SUCCESS);
    }

    @GetMapping("getPost")
    public BaseResponse<PostVO> getPost(Long postId) {
        Long userId = ThreadLocalUtils.get();
        PostVO vo = postService.getPost(postId, userId);
        return ResultUtils.success(vo);
    }

    @GetMapping("getPosts")
    public BaseResponse<List<PostVO>> getPosts(PageREQ getPostReq, HttpServletRequest request) {
        if (getPostReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        int curPage = getPostReq.getCurPage();
        int pageSize = getPostReq.getPageSize();
        Long userId = ThreadLocalUtils.get();
        List<PostVO> posts = postService.getPosts(userId, curPage, pageSize);
        return ResultUtils.success(posts);
    }

    @GetMapping("getPostByUserId")
    public BaseResponse getPostByUserId(PageREQ getPostReq) {
        int curPage = getPostReq.getCurPage();
        int pageSize = getPostReq.getPageSize();
        Long userId = ThreadLocalUtils.get();
        List<PostVO> vos = postService.getPostUid(curPage, pageSize, userId);
        return ResultUtils.success(vos);
    }

    @GetMapping("deletePost")
    public BaseResponse deletePost(Long postId) {
        return ResultUtils.success(postService.removeById(postId));
    }

    @GetMapping("getPostThumb")
    public BaseResponse getPostThumb(PageREQ getPostReq) {
        int curPage = getPostReq.getCurPage();
        int pageSize = getPostReq.getPageSize();
        Long userId = ThreadLocalUtils.get();
        List<PostVO> vos = postService.getPostThumb(curPage, pageSize, userId);
        return ResultUtils.success(vos);
    }

    @GetMapping("getPostCollect")
    public BaseResponse getPostCollect(PageREQ getPostReq) {
        int curPage = getPostReq.getCurPage();
        int pageSize = getPostReq.getPageSize();
        Long userId = ThreadLocalUtils.get();
        List<PostVO> vos = postService.getPostCollect(curPage, pageSize, userId);
        return ResultUtils.success(vos);
    }

    @PostMapping("getPostActions")
    public BaseResponse getPostActions(@RequestBody GetPostActionsREQ req) {
        List<PostVO> vos = postService.getPostActions(req);
        return ResultUtils.success(vos);
    }

}
