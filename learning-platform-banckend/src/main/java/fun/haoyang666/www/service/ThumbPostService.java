package fun.haoyang666.www.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.haoyang666.www.domain.entity.CollectPost;
import fun.haoyang666.www.domain.entity.ThumbPost;

/**
 * @author yang
 * @createTime 2023/1/16 11:50
 * @description
 */

public interface ThumbPostService extends IService<ThumbPost> {
    void thumb(long userId, long postId);


}
