package fun.haoyang666.www.admin.controller;

import fun.haoyang666.www.admin.UserParamReq;
import fun.haoyang666.www.admin.dto.SysUserDto;
import fun.haoyang666.www.annotation.CheckAuth;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.service.UserService;
import fun.haoyang666.www.utils.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/3/5 11:43
 * @description
 */
@RequestMapping("sysUser")
@RestController
@CheckAuth("admin")
public class SysUserController {

    @Resource
    private UserService userService;

    @PostMapping("listSysUser")
    public BaseResponse listSysUser(@RequestBody UserParamReq req) {
        List<SysUserDto> sysUser = userService.listSysUser(req);
        return ResultUtils.success(sysUser);
    }

    @GetMapping("forbiddenUser")
    public BaseResponse forbiddenUser(Long userId, Integer status) {
        return ResultUtils.success(userService.forbiddenUser(userId, status));
    }

    @GetMapping("removeUser")
    public BaseResponse removeUser(Long userId) {
        return ResultUtils.success(userService.removeUser(userId));
    }

    @GetMapping("resetUser")
    public BaseResponse resetUser(Long userId) {
        return ResultUtils.success(userService.resetUser(userId));

    }

    @PostMapping("updateUser")
    public BaseResponse updateUser(@RequestBody SysUserDto dto){
        return ResultUtils.success(userService.updateUser(dto));
    }
}
