package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.entity.ThumbPost;
import fun.haoyang666.www.mapper.PostMapper;
import fun.haoyang666.www.mapper.ThumbMapper;
import fun.haoyang666.www.service.ThumbPostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yang
 * @createTime 2023/1/16 12:03
 * @description
 */
@Service
public class ThumbPostServiceImpl extends ServiceImpl<ThumbMapper, ThumbPost> implements ThumbPostService {
    @Resource
    private PostMapper postMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void thumb(long userId, long postId) {
        ThumbPost thumb = this.lambdaQuery().eq(ThumbPost::getUserId, userId).eq(ThumbPost::getPostId, postId).one();
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            if (thumb == null) {
                ThumbPost thumbPost = new ThumbPost();
                thumbPost.setPostId(postId);
                thumbPost.setUserId(userId);
                this.save(thumbPost);
                postMapper.upThumb(postId);
            } else {
                this.removeById(thumb.getId());
                postMapper.downThumb(postId);
            }
        } finally {
            lock.unlock();
        }
    }

}
