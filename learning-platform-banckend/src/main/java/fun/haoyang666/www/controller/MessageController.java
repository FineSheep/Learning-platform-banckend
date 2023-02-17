package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.domain.dto.MessageCountDTO;
import fun.haoyang666.www.domain.dto.MessageResultDTO;
import fun.haoyang666.www.service.MessageService;
import fun.haoyang666.www.utils.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yang
 * @createTime 2023/2/16 10:14
 * @description
 */
@RestController
@RequestMapping("message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @GetMapping("commentMessage")
    public BaseResponse commentMessage(Long userId, Long curPage, Long pageSize) {
        MessageResultDTO thumbAndCollect = messageService.commentMessage(userId, curPage, pageSize);
        return ResultUtils.success(thumbAndCollect);
    }

    @GetMapping("dotMessage")
    public BaseResponse dotMessage(Long userId) {
        Long count = messageService.dotMessage(userId);
        return ResultUtils.success(count);
    }

    @GetMapping("dotMessageAll")
    public BaseResponse dotMessageAll(Long userId) {
        MessageCountDTO dto = messageService.dotMessageAll(userId);
        return ResultUtils.success(dto);
    }

    @GetMapping("readAllComment")
    public BaseResponse readAll(Long userId) {
        return ResultUtils.success(messageService.readAllComment(userId));
    }

    @GetMapping("removeAllComment")
    public BaseResponse removeAllComment(Long userId) {
        return ResultUtils.success(messageService.removeAllComment(userId));
    }

    @GetMapping("removeComment")
    public BaseResponse removeComment(Long id) {
        return ResultUtils.success(messageService.removeComment(id));
    }
}
