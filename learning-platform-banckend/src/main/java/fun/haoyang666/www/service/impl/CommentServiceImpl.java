package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.entity.Comment;
import fun.haoyang666.www.mapper.CommentMapper;
import fun.haoyang666.www.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @createTime 2023/2/10 15:02
 * @description
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}
