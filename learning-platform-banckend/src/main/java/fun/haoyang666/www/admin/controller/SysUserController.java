package fun.haoyang666.www.admin.controller;

import fun.haoyang666.www.admin.UserParamReq;
import fun.haoyang666.www.admin.dto.SysUserDto;
import fun.haoyang666.www.annotation.CheckAuth;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.service.UserService;
import fun.haoyang666.www.utils.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/3/5 11:43
 * @description
 */
@RequestMapping("sysUser")
@RestController
public class SysUserController {

    @Resource
    private UserService userService;

    @PostMapping("listSysUser")
    @CheckAuth("admin")
    public BaseResponse listSysUser(UserParamReq req){
        List<SysUserDto> sysUser = userService.listSysUser(req);
        return ResultUtils.success(sysUser);
    }
}
