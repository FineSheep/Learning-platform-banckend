package fun.haoyang666.www.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.haoyang666.www.domain.entity.CollectPost;

/**
 * @author yang
 * @createTime 2023/1/16 11:50
 * @description
 */

public interface CollectPostService extends IService<CollectPost> {


    void collect(long userId, long postId);
}
