package fun.haoyang666.www.admin.controller;

import fun.haoyang666.www.admin.dto.MessageParamDto;
import fun.haoyang666.www.admin.dto.MessageSendDto;
import fun.haoyang666.www.annotation.CheckAuth;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.service.MessageService;
import fun.haoyang666.www.service.UserService;
import fun.haoyang666.www.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author yang
 * @createTime 2023/3/11 14:32
 * @description
 */
@RequestMapping("sysMessage")
@RestController
@CheckAuth("admin")
public class SysMessageController {
    @Resource
    private MessageService messageService;

    @PostMapping("messageSend")
    public BaseResponse messageSend(@RequestBody MessageSendDto messageSendDto) {
        return ResultUtils.success(messageService.messageSend(messageSendDto));
    }

    @PostMapping("listMessage")
    public BaseResponse listMessage(@RequestBody MessageParamDto messageParamDto) {
        return ResultUtils.success(messageService.listMessage(messageParamDto));
    }

    @GetMapping("dealMessage")
    public BaseResponse dealMessage(Long id, Integer deal) {
        if (id == null || deal == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(messageService.dealMessage(id, deal));
    }
}
