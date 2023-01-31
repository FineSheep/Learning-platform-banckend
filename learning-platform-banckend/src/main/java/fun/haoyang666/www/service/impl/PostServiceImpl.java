package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.dto.ScrollerDTO;
import fun.haoyang666.www.domain.entity.CollectPost;
import fun.haoyang666.www.domain.entity.Post;
import fun.haoyang666.www.domain.entity.Tag;
import fun.haoyang666.www.domain.entity.ThumbPost;
import fun.haoyang666.www.domain.vo.PostVO;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.CollectPostService;
import fun.haoyang666.www.service.PostService;
import fun.haoyang666.www.mapper.PostMapper;
import fun.haoyang666.www.service.TagService;
import fun.haoyang666.www.service.ThumbPostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private TagService tagService;
    @Resource
    private CollectPostService collectPostService;
    @Resource
    private ThumbPostService thumbPostService;

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
    public ScrollerDTO<PostVO> getPosts(long userId, int curPage, int pageSize) {
        ScrollerDTO<PostVO> dto = new ScrollerDTO<>();
        long count = this.count();
        int offset = 0;
        if ((curPage - 1) * pageSize > 0) {
            offset = (curPage - 1) * pageSize;
        }
        //获取标签，转换为map
        Map<Long, String> tagMap = tagService.list().stream().collect(Collectors.toMap(Tag::getId, Tag::getTagName));
        List<PostVO> postVOS = postMapper.selectPosts(offset, pageSize);
        //标签转化为名字
        postVOS.forEach(item -> {
            String tagsStr = item.getTags();
            Gson gson = new Gson();
            Long[] tags = gson.fromJson(tagsStr, Long[].class);
            ArrayList<String> tagsName = new ArrayList<>();
            Arrays.stream(tags).forEach(index -> {
                tagsName.add(tagMap.get(index));
            });
            //判断是否收藏，点赞
            item.setTagsName(tagsName);
            if (isCollected(userId, item.getId())) {
                item.setCollected(true);
            }
            if (isThumbed(userId, item.getId())) {
                item.setThumbed(true);
            }
        });
        dto.setRecords(postVOS);
        if (count <= (long) curPage * pageSize) {
            dto.setHasNext(false);
        } else {
            dto.setHasNext(true);
        }
        return dto;
    }

    private boolean isCollected(long userId, long postId) {
        List<Long> collects = collectPostService.lambdaQuery().eq(CollectPost::getUserId, userId).list()
                .stream().map(CollectPost::getPostId).collect(Collectors.toList());
        if (collects.contains(postId)) {
            return true;
        }
        return false;
    }

    private boolean isThumbed(long userId, long postId) {
        List<Long> thumbs = thumbPostService.lambdaQuery().eq(ThumbPost::getUserId, userId).list()
                .stream().map(ThumbPost::getPostId).collect(Collectors.toList());
        if (thumbs.contains(postId)) {
            return true;
        }
        return false;
    }
}




