package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.domain.req.UpdatePasswordREQ;
import fun.haoyang666.www.domain.req.UserInfoREQ;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.dto.UserDTO;
import fun.haoyang666.www.domain.dto.UserInfoDTO;
import fun.haoyang666.www.domain.req.UserLoginByCodeREQ;
import fun.haoyang666.www.domain.req.UserLoginByPassWordREQ;
import fun.haoyang666.www.service.UserService;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yang
 * @createTime 2022/12/17 21:55
 * @description
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("updateUserInfo")
    public BaseResponse updateUserInfo(@RequestBody UserInfoREQ req) {
        userService.updateUserInfo(req);
        return ResultUtils.success(Constant.SUCCESS);
    }

    @GetMapping("getCode")
    public BaseResponse getCode(String email) {
        if (StringUtils.isBlank(email)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        userService.getCode(email);
        return ResultUtils.success("验证码发送成功");
    }

    @PostMapping("loginByPassword")
    public BaseResponse<UserDTO> loginByPassword(@RequestBody UserLoginByPassWordREQ userLoginByPassWordReq, HttpServletRequest request, HttpServletResponse response) {
        if (userLoginByPassWordReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String email = userLoginByPassWordReq.getEmail();
        String password = userLoginByPassWordReq.getPassword();
        if (StringUtils.isAnyBlank(email, password)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        UserDTO userDto = userService.userLogin(email, password, request, response);
        return ResultUtils.success(userDto);
    }

    @PostMapping("loginOrRegister")
    public BaseResponse<UserDTO> loginByCode(@RequestBody UserLoginByCodeREQ userLoginByCodeReq, HttpServletRequest request, HttpServletResponse response) {
        if (userLoginByCodeReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String email = userLoginByCodeReq.getEmail();
        String code = userLoginByCodeReq.getCode();
        if (StringUtils.isAnyBlank(email, code)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        UserDTO userDto = userService.loginOrRegister(email, code, request, response);
        return ResultUtils.success(userDto);
    }

    @GetMapping("userInfo")
    public BaseResponse<UserInfoDTO> userInfo() {
        Long userId = ThreadLocalUtils.get();
        UserInfoDTO userDto = userService.userInfo(userId);
        return ResultUtils.success(userDto);
    }

    @GetMapping("getCodeById")
    public BaseResponse getCodeById() {
        Long userId = ThreadLocalUtils.get();
        userService.getCodeById(userId);
        return ResultUtils.success("success");
    }

    @PostMapping("updatePassword")
    public BaseResponse updatePassword(@RequestBody UpdatePasswordREQ req) {
        return ResultUtils.success(userService.updatePassword(req));
    }

    @GetMapping("person")
    public BaseResponse<UserInfoDTO> userInfo(Long personId) {
        UserInfoDTO userDto = userService.userInfo(personId);
        return ResultUtils.success(userDto);
    }

}
