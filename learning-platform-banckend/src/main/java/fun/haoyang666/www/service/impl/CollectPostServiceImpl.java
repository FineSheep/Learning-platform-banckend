package fun.haoyang666.www.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.entity.CollectPost;
import fun.haoyang666.www.mapper.CollectPostMapper;
import fun.haoyang666.www.mapper.PostMapper;
import fun.haoyang666.www.service.CollectPostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yang
 * @createTime 2023/1/16 11:51
 * @description
 */
@Service
public class CollectPostServiceImpl extends ServiceImpl<CollectPostMapper, CollectPost>
        implements CollectPostService {

    @Resource
    private PostMapper postMapper;

    @Override
    public void collect(long userId, long postId) {
        CollectPost collect = this.lambdaQuery().eq(CollectPost::getUserId, userId).eq(CollectPost::getPostId, postId).one();
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            if (collect == null) {
                CollectPost collectPost = new CollectPost();
                collectPost.setPostId(postId);
                collectPost.setUserId(userId);
                this.save(collectPost);
                postMapper.upCollect(postId);
            } else {
                this.removeById(collect.getId());
                postMapper.downCollect(postId);
            }
        } finally {
            lock.unlock();
        }
    }

}
