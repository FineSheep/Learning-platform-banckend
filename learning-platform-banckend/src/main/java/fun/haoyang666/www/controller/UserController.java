package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.ResultUtils;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.req.UserRegisterReq;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("register")
    public BaseResponse<Long> register(@RequestBody UserRegisterReq registerReq) {
        if (registerReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String email = registerReq.getEmail();
        String password = registerReq.getPassword();
        String userCode = registerReq.getUserCode();
        if (StringUtils.isAnyBlank(email, password, userCode)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Long id = userService.userRegister(email, password, userCode);
        return ResultUtils.success(id);
    }

    @GetMapping("getCode")
    public BaseResponse getCode(String email) {
        if (StringUtils.isBlank(email)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        userService.getCode(email);
        return ResultUtils.success("验证码发送成功");
    }
}
