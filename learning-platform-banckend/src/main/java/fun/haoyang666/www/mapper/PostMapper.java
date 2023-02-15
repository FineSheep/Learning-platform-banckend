package fun.haoyang666.www.mapper;

import fun.haoyang666.www.domain.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.haoyang666.www.domain.req.GetPostActionsREQ;
import fun.haoyang666.www.domain.vo.PostVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description 针对表【post(帖子)】的数据库操作Mapper
 * @createDate 2022-12-10 16:20:50
 * @Entity com.haoyang666.fun.domain.Post
 */
public interface PostMapper extends BaseMapper<Post> {

    int downThumb(long postId);

    List<PostVO> selectPosts(int offset, int pageSize);

    List<PostVO> selectPostsUid(int offset, int pageSize, long uid);

    int upThumb(long postId);

    int downCollect(long postId);

    int upCollect(long postId);

    void updatePostComment(Long postId);


    List<PostVO> selectPostThumb(int offset, int pageSize, Long userId);

    List<PostVO> selectPostCollect(int offset, int pageSize, Long userId);

    List<PostVO> getPostActions(GetPostActionsREQ req);
}




