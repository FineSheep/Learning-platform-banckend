package fun.haoyang666.www.mapper;

import fun.haoyang666.www.domain.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.haoyang666.www.domain.vo.PostVo;

import java.util.List;

/**
 * @author yang
 * @description 针对表【post(帖子)】的数据库操作Mapper
 * @createDate 2022-12-10 16:20:50
 * @Entity com.haoyang666.fun.domain.Post
 */
public interface PostMapper extends BaseMapper<Post> {

    List<PostVo> selectPosts(int offset, int pageSize);

    int downThumb(long postId);

    int upThumb(long postId);

    int downCollect(long postId);

    int upCollect(long postId);

}




