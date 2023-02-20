package fun.haoyang666.www.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.haoyang666.www.domain.dto.MessageCountDTO;
import fun.haoyang666.www.domain.dto.MessageResultDTO;
import fun.haoyang666.www.domain.entity.Comment;
import fun.haoyang666.www.domain.entity.Message;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface MessageService extends IService<Message> {
    void thumbAndCollect(Long postId, Long userId);

    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    void commentMessage(Comment comment, Long personId);

    MessageResultDTO commentMessage(Long userId, Long curPage, Long pageSize);

    Long dotMessage(Long userId);

    MessageCountDTO dotMessageAll(Long userId);

    Boolean readAllComment(Long userId);

    Boolean removeAllComment(Long userId);

    Boolean removeMessage(Long id);

    MessageResultDTO thumbCollectMessage(Long userId, Long curPage, Long pageSize);

    Boolean readAllThumbCollectMessage(Long userId);

    Boolean removeAllThumbCollectMessage(Long userId);

    MessageResultDTO systemMessage(Long userId, Long curPage, Long pageSize);

    Boolean readAllSystemMessage(Long userId);

    Boolean removeAllSystemMessage(Long userId);

    Boolean readMessage(Long messageId);
}
