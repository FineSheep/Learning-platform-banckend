package fun.haoyang666.www.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
