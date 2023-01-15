package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.dto.ScrollerDto;
import fun.haoyang666.www.domain.entity.Post;
import fun.haoyang666.www.domain.vo.PostVo;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.PostService;
import fun.haoyang666.www.mapper.PostMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yang
 * @description 针对表【post(帖子)】的数据库操作Service实现
 * @createDate 2022-12-10 16:20:50
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
        implements PostService {

    @Resource
    private PostMapper postMapper;

    @Override
    public void savePost(String content, String title, String description, String photo, long userId, List<Long> tags) {
        Gson gson = new Gson();
        String jsonTags = gson.toJson(tags);
        Post post = new Post();

        post.setDescription(description);
        post.setContent(content);
        post.setPhoto(photo);
        post.setUserId(userId);
        post.setTags(jsonTags);
        post.setTitle(title);
        boolean save = this.save(post);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

    }

    @Override
    public ScrollerDto<PostVo> getPosts(int curPage, int pageSize) {
        ScrollerDto<PostVo> dto = new ScrollerDto<>();
        long count = this.count();
        int offset =0;
        if ((curPage - 1) * pageSize >0){
            offset = (curPage - 1) * pageSize;
        }
        List<PostVo> postVos = postMapper.selectPosts(offset, pageSize);
        dto.setRecords(postVos);
        if (count <= (long) curPage * pageSize) {
            dto.setHasNext(false);
        } else {
            dto.setHasNext(true);
        }
        return dto;
    }
}




