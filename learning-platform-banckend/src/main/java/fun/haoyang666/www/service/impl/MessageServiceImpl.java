package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.admin.dto.MessageParamDto;
import fun.haoyang666.www.admin.dto.MessageSendDto;
import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.ReadMenum;
import fun.haoyang666.www.common.enums.SysMessageEnum;
import fun.haoyang666.www.domain.dto.MessageCountDTO;
import fun.haoyang666.www.domain.dto.MessageResultDTO;
import fun.haoyang666.www.domain.dto.MessageThumbCollectDTO;
import fun.haoyang666.www.domain.dto.ReportDto;
import fun.haoyang666.www.domain.entity.*;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.mapper.MessageMapper;
import fun.haoyang666.www.mapper.PostMapper;
import fun.haoyang666.www.mapper.UserMapper;
import fun.haoyang666.www.service.MessageService;
import fun.haoyang666.www.service.MessageUserService;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yang
 * @createTime 2023/2/16 10:05
 * @description
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Resource
    private PostMapper postMapper;
    @Resource
    private MessageUserService messageUserService;
    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void thumbAndCollect(Long postId, Long userId) {
        Post post = postMapper.selectById(postId);
        //发送点赞消息
        Message message = new Message();
        message.setPostId(postId);
        message.setCreateId(userId);
        message.setType(SysMessageEnum.THUMB_COLLECT.getCode());
        message.setTitle(post.getTitle());
        this.save(message);
        //存储用户信息表
        Long createId = post.getUserId();
        MessageUser messageUser = new MessageUser();
        messageUser.setMessageId(message.getId());
        messageUser.setUserId(createId);
        messageUser.setType(SysMessageEnum.THUMB_COLLECT.getCode());
        messageUserService.save(messageUser);
    }

    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    @Override
    public void commentMessage(Comment comment, Long personId) {
        Long postId = comment.getPostId();
        Post post = postMapper.selectById(postId);
        Message message = new Message();
        message.setType(SysMessageEnum.COMMENT.getCode());
        message.setPostId(postId);
        message.setCreateId(comment.getUserId());
        message.setContent(comment.getContent());
        message.setTitle(post.getTitle());
        this.save(message);
        MessageUser messageUser = new MessageUser();
        messageUser.setMessageId(message.getId());
        messageUser.setType(SysMessageEnum.COMMENT.getCode());
        if (personId == null) {
            Long userId = post.getUserId();
            messageUser.setUserId(userId);
        } else {
            messageUser.setUserId(personId);
        }
        messageUserService.save(messageUser);
    }


    @Override
    public Long dotMessage(Long userId) {
        Long count = messageUserService.lambdaQuery().eq(MessageUser::getUserId, userId)
                .eq(MessageUser::getIsRead, ReadMenum.UN_READ.getCode()).count();
        return count;
    }

    @Override
    public MessageCountDTO dotMessageAll(Long userId) {
        Map<Integer, List<MessageUser>> map = messageUserService.lambdaQuery()
                .eq(MessageUser::getUserId, userId)
                .eq(MessageUser::getIsRead, ReadMenum.UN_READ.getCode())
                .list().stream().collect(Collectors.groupingBy(MessageUser::getType));
        MessageCountDTO dto = new MessageCountDTO();
        for (Integer type : map.keySet()) {
            if (SysMessageEnum.COMMENT.getCode().equals(type)) {
                dto.setComment(map.get(type).size());
            } else if (SysMessageEnum.THUMB_COLLECT.getCode().equals(type)) {
                dto.setThumbCollect(map.get(type).size());
            } else if (SysMessageEnum.SYSTEM.getCode().equals(type)) {
                dto.setSystem(map.get(type).size());
            }
        }
        return dto;
    }


    private MessageResultDTO getDTO(Page<MessageUser> page) {
        MessageResultDTO resultDTO = new MessageResultDTO();
        resultDTO.setCount(page.getTotal());
        List<MessageUser> list = page.getRecords();
        LinkedList<MessageThumbCollectDTO> linkedList = new LinkedList<>();
        for (MessageUser messageUser : list) {
            MessageThumbCollectDTO dto = new MessageThumbCollectDTO();
            Long messageId = messageUser.getMessageId();
            Message message = this.getById(messageId);
            Long createId = message.getCreateId();
            User user = userMapper.selectById(createId);
            dto.setId(messageUser.getId());
            dto.setPostId(message.getPostId());
            dto.setSendId(createId);
            dto.setSendName(user.getUsername());
            dto.setSendAvatar(user.getAvatarUrl());
            dto.setMessageId(messageId);
            dto.setTitle(message.getTitle());
            dto.setContent(message.getContent());
            dto.setIsRead(messageUser.getIsRead());
            dto.setTime(messageUser.getCreateTime());
            linkedList.add(dto);
        }
        resultDTO.setData(linkedList);
        return resultDTO;
    }

    /**
     * 针对评论消息的操作
     */
    @Override
    public MessageResultDTO commentMessage(Long userId, Long curPage, Long pageSize) {
        Page<MessageUser> page = messageUserService.lambdaQuery().eq(MessageUser::getUserId, userId)
                .eq(MessageUser::getType, SysMessageEnum.COMMENT.getCode())
                .orderByDesc(MessageUser::getCreateTime).page(new Page<>(curPage, pageSize));
        return getDTO(page);
    }

    @Override
    public Boolean readAllComment(Long userId) {
        return messageUserService.lambdaUpdate()
                .eq(MessageUser::getUserId, userId).eq(MessageUser::getType, SysMessageEnum.COMMENT.getCode())
                .set(MessageUser::getIsRead, ReadMenum.READ.getCode()).update();
    }

    @Override
    public Boolean removeAllComment(Long userId) {
        LambdaQueryWrapper<MessageUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageUser::getUserId, userId)
                .eq(MessageUser::getType, SysMessageEnum.COMMENT.getCode());
        return messageUserService.remove(wrapper);
    }

    @Override
    public Boolean removeMessage(Long id) {
        return messageUserService.removeById(id);
    }


    /**
     * 针对点赞收藏
     */
    @Override
    public MessageResultDTO thumbCollectMessage(Long userId, Long curPage, Long pageSize) {
        Page<MessageUser> page = messageUserService.lambdaQuery().eq(MessageUser::getUserId, userId)
                .eq(MessageUser::getType, SysMessageEnum.THUMB_COLLECT.getCode())
                .orderByDesc(MessageUser::getCreateTime).page(new Page<>(curPage, pageSize));
        return getDTO(page);
    }

    @Override
    public Boolean readAllThumbCollectMessage(Long userId) {
        return messageUserService.lambdaUpdate()
                .eq(MessageUser::getUserId, userId).eq(MessageUser::getType, SysMessageEnum.THUMB_COLLECT.getCode())
                .set(MessageUser::getIsRead, ReadMenum.READ.getCode()).update();
    }

    @Override
    public Boolean removeAllThumbCollectMessage(Long userId) {
        LambdaQueryWrapper<MessageUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageUser::getUserId, userId)
                .eq(MessageUser::getType, SysMessageEnum.THUMB_COLLECT.getCode());
        return messageUserService.remove(wrapper);
    }

    /**
     * 系统通知
     */
    @Override
    public MessageResultDTO systemMessage(Long userId, Long curPage, Long pageSize) {
        Page<MessageUser> page = messageUserService.lambdaQuery().eq(MessageUser::getUserId, userId)
                .eq(MessageUser::getType, SysMessageEnum.SYSTEM.getCode())
                .orderByDesc(MessageUser::getCreateTime).page(new Page<>(curPage, pageSize));
        MessageResultDTO resultDTO = new MessageResultDTO();
        resultDTO.setCount(page.getTotal());
        List<MessageUser> list = page.getRecords();
        LinkedList<MessageThumbCollectDTO> linkedList = new LinkedList<>();
        for (MessageUser messageUser : list) {
            MessageThumbCollectDTO dto = new MessageThumbCollectDTO();
            Long messageId = messageUser.getMessageId();
            Message message = this.getById(messageId);
            Long createId = message.getCreateId();
            dto.setId(messageUser.getId());
            dto.setPostId(message.getPostId());
            dto.setSendId(createId);
            dto.setMessageId(messageId);
            dto.setTitle(message.getTitle());
            dto.setContent(message.getContent());
            dto.setIsRead(messageUser.getIsRead());
            dto.setTime(messageUser.getCreateTime());
            linkedList.add(dto);
        }
        resultDTO.setData(linkedList);
        return resultDTO;
    }

    @Override
    public Boolean readAllSystemMessage(Long userId) {
        return messageUserService.lambdaUpdate()
                .eq(MessageUser::getUserId, userId).eq(MessageUser::getType, SysMessageEnum.SYSTEM.getCode())
                .set(MessageUser::getIsRead, ReadMenum.READ.getCode()).update();
    }

    @Override
    public Boolean removeAllSystemMessage(Long userId) {
        LambdaQueryWrapper<MessageUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageUser::getUserId, userId)
                .eq(MessageUser::getType, SysMessageEnum.SYSTEM.getCode());
        return messageUserService.remove(wrapper);
    }

    @Override
    public Boolean readMessage(Long messageId) {
        LambdaUpdateWrapper<MessageUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(MessageUser::getMessageId, messageId).eq(MessageUser::getIsRead, ReadMenum.UN_READ.getCode());
        wrapper.set(MessageUser::getIsRead, ReadMenum.READ.getCode());
        return messageUserService.update(wrapper);
    }

    @Override
    public Boolean messageSend(MessageSendDto messageSendDto) {
        String content = messageSendDto.getContent();
        String type = messageSendDto.getType();
        Message message = new Message();
        message.setTitle("系统通知");
        message.setContent(content);
        message.setType(SysMessageEnum.SYSTEM.getCode());
        message.setCreateId(ThreadLocalUtils.get().getUserId());
        this.save(message);
        if (Constant.MESS_TYPE_SINGLE.equals(type)) {
            Long userId = messageSendDto.getUserId();
            MessageUser messageUser = new MessageUser();
            messageUser.setMessageId(message.getId());
            messageUser.setType(SysMessageEnum.SYSTEM.getCode());
            messageUser.setUserId(userId);
            messageUserService.save(messageUser);
        } else {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.ne(User::getUserRole, Constant.ADMIN);
            List<Long> userIds = userMapper.selectList(queryWrapper)
                    .stream().map(User::getId).collect(Collectors.toList());
            LinkedList<MessageUser> list = new LinkedList<>();
            userIds.forEach(id -> {
                MessageUser messageUser = new MessageUser();
                messageUser.setMessageId(message.getId());
                messageUser.setType(SysMessageEnum.SYSTEM.getCode());
                messageUser.setUserId(id);
                list.add(messageUser);
            });
            messageUserService.saveBatch(list);
        }
        return true;
    }

    @Override
    public List<Message> listMessage(MessageParamDto messageParamDto) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        Integer type = messageParamDto.getType();
        String title = messageParamDto.getTitle();
        String content = messageParamDto.getContent();
        Long id = messageParamDto.getId();
        Long deal = messageParamDto.getDeal();
        Long userId = messageParamDto.getUserId();
        queryWrapper.ne(Message::getType, SysMessageEnum.THUMB_COLLECT.getCode());
        queryWrapper.eq(id != null, Message::getId, id);
        queryWrapper.like(StringUtils.isNotBlank(content), Message::getContent, content);
        queryWrapper.eq(type != null, Message::getType, type);
        queryWrapper.like(StringUtils.isNotBlank(title), Message::getTitle, title);
        queryWrapper.eq(userId != null, Message::getCreateId, userId);
        queryWrapper.eq(deal != null, Message::getDeal, deal);
        List<Message> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public Boolean dealMessage(Long id, Integer deal) {
        boolean b = this.lambdaUpdate().eq(Message::getId, id).set(Message::getDeal, deal).update();
        return b;
    }

    @Override
    public Boolean reportPost(ReportDto reportDto) {
        Long id = reportDto.getId();
        String content = reportDto.getContent();
        if (id == null || StringUtils.isBlank(content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Message message = new Message();
        message.setTitle("举报通知");
        message.setType(SysMessageEnum.REPORT.getCode());
        message.setContent(content);
        message.setCreateId(ThreadLocalUtils.get().getUserId());
        message.setDeal(Constant.NO_DEAL);
        message.setPostId(id);
        return this.save(message);
    }
}
