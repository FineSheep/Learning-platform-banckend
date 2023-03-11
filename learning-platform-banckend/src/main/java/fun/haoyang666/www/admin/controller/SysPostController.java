package fun.haoyang666.www.admin.controller;

import fun.haoyang666.www.admin.dto.CheckPost;
import fun.haoyang666.www.admin.dto.SysPostDto;
import fun.haoyang666.www.annotation.CheckAuth;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.service.PostService;
import fun.haoyang666.www.utils.ResultUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yang
 * @createTime 2023/3/7 12:10
 * @description
 */
@RequestMapping("sysPost")
@RestController
@CheckAuth("admin")
public class SysPostController {
    @Resource
    private PostService postService;

    @PostMapping("listPost")
    public BaseResponse listPost(@RequestBody SysPostDto postDto) {
        return ResultUtils.success(postService.listPost(postDto));
    }

    @PostMapping("checkPost")
    public BaseResponse checkPost(@RequestBody CheckPost checkPost){
        return ResultUtils.success(postService.checkPost(checkPost));
    }

}
