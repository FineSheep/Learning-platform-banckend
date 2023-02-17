package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.common.enums.SysMessageEnum;
import fun.haoyang666.www.domain.dto.MessageResultDTO;
import fun.haoyang666.www.domain.dto.MessageThumbCollectDTO;
import fun.haoyang666.www.domain.entity.*;
import fun.haoyang666.www.mapper.MessageMapper;
import fun.haoyang666.www.mapper.PostMapper;
import fun.haoyang666.www.mapper.UserMapper;
import fun.haoyang666.www.service.MessageService;
import fun.haoyang666.www.service.MessageUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

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
        //发送点赞消息
        Message message = new Message();
        message.setPostId(postId);
        message.setCreateId(userId);
        message.setType(Long.valueOf(SysMessageEnum.THUMB_COLLECT.getCode()));
        this.save(message);
        //存储用户信息表
        Post post = postMapper.selectById(postId);
        Long createId = post.getUserId();
        MessageUser messageUser = new MessageUser();
        messageUser.setMessageId(message.getId());
        messageUser.setUserId(createId);
        messageUser.setType(Long.valueOf(SysMessageEnum.THUMB_COLLECT.getCode()));
        messageUserService.save(messageUser);
    }

    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    @Override
    public void commentMessage(Comment comment, Long personId) {
        Long postId = comment.getPostId();
        Post post = postMapper.selectById(postId);
        Message message = new Message();
        message.setType(Long.valueOf(SysMessageEnum.COMMENT.getCode()));
        message.setPostId(postId);
        message.setCreateId(comment.getUserId());
        message.setContent(comment.getContent());
        message.setTitle(post.getTitle());
        this.save(message);
        MessageUser messageUser = new MessageUser();
        messageUser.setMessageId(message.getId());
        messageUser.setType(Long.valueOf(SysMessageEnum.COMMENT.getCode()));
        if (personId == null) {
            Long userId = post.getUserId();
            messageUser.setUserId(userId);
        } else {
            messageUser.setUserId(personId);
        }
        messageUserService.save(messageUser);
    }

    @Override
    public MessageResultDTO commentMessage(Long userId, Long curPage, Long pageSize) {
        MessageResultDTO resultDTO = new MessageResultDTO();
        Page<MessageUser> page = messageUserService.lambdaQuery().eq(MessageUser::getUserId, userId).eq(MessageUser
        ::getType, Long.valueOf(SysMessageEnum.COMMENT.getCode()))
                .orderByDesc(MessageUser::getCreateTime).page(new Page<>(curPage, pageSize));
        resultDTO.setCount(page.getTotal());
        List<MessageUser> list = page.getRecords();
        LinkedList<MessageThumbCollectDTO> linkedList = new LinkedList<>();
        for (MessageUser messageUser : list) {
            MessageThumbCollectDTO dto = new MessageThumbCollectDTO();
            Long messageId = messageUser.getMessageId();
            Message message = this.getById(messageId);
            Long createId = message.getCreateId();
            User user = userMapper.selectById(createId);
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

}
