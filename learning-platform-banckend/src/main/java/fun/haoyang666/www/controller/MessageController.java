package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.domain.dto.MessageCountDTO;
import fun.haoyang666.www.domain.dto.MessageResultDTO;
import fun.haoyang666.www.service.MessageService;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.utils.ThreadLocalUtils;
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
    public BaseResponse commentMessage(Long curPage, Long pageSize) {
        Long userId = ThreadLocalUtils.get().getUserId();
        MessageResultDTO commentMessage = messageService.commentMessage(userId, curPage, pageSize);
        return ResultUtils.success(commentMessage);
    }

    @GetMapping("dotMessage")
    public BaseResponse dotMessage() {
        Long userId = ThreadLocalUtils.get().getUserId();
        Long count = messageService.dotMessage(userId);
        return ResultUtils.success(count);
    }

    @GetMapping("dotMessageAll")
    public BaseResponse dotMessageAll() {
        Long userId = ThreadLocalUtils.get().getUserId();
        MessageCountDTO dto = messageService.dotMessageAll(userId);
        return ResultUtils.success(dto);
    }

    @GetMapping("readAllComment")
    public BaseResponse readAllComment() {
        Long userId = ThreadLocalUtils.get().getUserId();
        return ResultUtils.success(messageService.readAllComment(userId));
    }

    @GetMapping("removeAllComment")
    public BaseResponse removeAllComment() {
        Long userId = ThreadLocalUtils.get().getUserId();
        return ResultUtils.success(messageService.removeAllComment(userId));
    }

    @GetMapping("removeComment")
    public BaseResponse removeComment(Long id) {
        return ResultUtils.success(messageService.removeMessage(id));
    }

    @GetMapping("thumbCollectMessage")
    public BaseResponse thumbCollectMessage( Long curPage, Long pageSize) {
        Long userId = ThreadLocalUtils.get().getUserId();
        MessageResultDTO commentMessage = messageService.thumbCollectMessage(userId, curPage, pageSize);
        return ResultUtils.success(commentMessage);
    }

    @GetMapping("readAllThumbCollectMessage")
    public BaseResponse readAllThumbCollectMessage() {
        Long userId = ThreadLocalUtils.get().getUserId();
        return ResultUtils.success(messageService.readAllThumbCollectMessage(userId));
    }

    @GetMapping("removeAllThumb")
    public BaseResponse removeAllThumb() {
        Long userId = ThreadLocalUtils.get().getUserId();
        return ResultUtils.success(messageService.removeAllThumbCollectMessage(userId));
    }

    @GetMapping("readAllSystemMessage")
    public BaseResponse readAllSystemMessage() {
        Long userId = ThreadLocalUtils.get().getUserId();
        return ResultUtils.success(messageService.readAllSystemMessage(userId));
    }


    @GetMapping("systemMessage")
    public BaseResponse systemMessage( Long curPage, Long pageSize) {
        Long userId = ThreadLocalUtils.get().getUserId();
        MessageResultDTO commentMessage = messageService.systemMessage(userId, curPage, pageSize);
        return ResultUtils.success(commentMessage);
    }

    @GetMapping("removeAllSystem")
    public BaseResponse removeAllSystem() {
        Long userId = ThreadLocalUtils.get().getUserId();
        return ResultUtils.success(messageService.removeAllSystemMessage(userId));
    }

    @GetMapping("readMessage")
    public BaseResponse readMessage(Long messageId){
        return ResultUtils.success(messageService.readMessage(messageId));

    }
}
