package fun.haoyang666.www.service.impl;

import cn.hutool.core.date.StopWatch;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import fun.haoyang666.www.admin.dto.CheckPostDto;
import fun.haoyang666.www.admin.dto.SysPostDto;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.StatusEnum;
import fun.haoyang666.www.domain.entity.*;
import fun.haoyang666.www.domain.req.GetPostActionsREQ;
import fun.haoyang666.www.domain.vo.PostVO;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.mapper.PostMapper;
import fun.haoyang666.www.service.CollectPostService;
import fun.haoyang666.www.service.PostService;
import fun.haoyang666.www.service.TagService;
import fun.haoyang666.www.service.ThumbPostService;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import fun.haoyang666.www.utils.ThreadPool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author yang
 * @description 针对表【post(帖子)】的数据库操作Service实现
 * @createDate 2022-12-10 16:20:50
 */
@Service
@Slf4j
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
    @SneakyThrows
    public List<PostVO> getPosts(long userId, int curPage, int pageSize) {
        int offset = 0;
        if ((curPage - 1) * pageSize > 0) {
            offset = (curPage - 1) * pageSize;
        }
        ExecutorService instance = ThreadPool.instance();
        //获取标签，转换为map
        Map<Long, String> tagMap = tagService.list().stream().collect(Collectors.toMap(Tag::getId, Tag::getTagName));
        List<PostVO> postVOS = postMapper.selectPosts(offset, pageSize);
        //标签转化为名字,并行流操作
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CountDownLatch count = new CountDownLatch(postVOS.size());
        postVOS.forEach(item -> {
            instance.execute(()->{
                String tagsStr = item.getTags();
                Gson gson = new Gson();
                Long[] tags = gson.fromJson(tagsStr, Long[].class);
                if (tags != null) {
                    ArrayList<String> tagsName = new ArrayList<>();
                    Arrays.stream(tags).forEach(index -> {
                        tagsName.add(tagMap.get(index));
                    });
                    item.setTagsName(tagsName);
                }
                //判断是否收藏，点赞
                if (isCollected(userId, item.getId())) {
                    item.setCollected(true);
                }
                if (isThumbed(userId, item.getId())) {
                    item.setThumbed(true);
                }
//                log.info("当前线程：{}",Thread.currentThread().getId());
                count.countDown();
            });
        });
        count.await();
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("查询用时：{}",totalTimeMillis);
        return postVOS;
    }

    @Override
    public PostVO getPost(long id, long userId) {
        Post post = this.lambdaQuery().eq(Post::getId, id).one();
        PostVO vo = new PostVO();
        BeanUtils.copyProperties(post, vo);
        vo.setCollected(isCollected(userId, id));
        vo.setThumbed(isThumbed(userId, id));
        User user = new User();
        user.setId(post.getUserId());
        vo.setUser(user);
        return vo;
    }

    @Override
    public List<PostVO> getPostUid(int curPage, int pageSize, Long userId) {
        int offset = 0;
        if ((curPage - 1) * pageSize > 0) {
            offset = (curPage - 1) * pageSize;
        }
        List<PostVO> postVOS = postMapper.selectPostsUid(offset, pageSize, userId);
        return postVOS;
    }

    @Override
    public List<PostVO> getPostThumb(int curPage, int pageSize, Long userId) {
        int offset = 0;
        if ((curPage - 1) * pageSize > 0) {
            offset = (curPage - 1) * pageSize;
        }
        List<PostVO> postVOS = postMapper.selectPostThumb(offset, pageSize, userId);
        return postVOS;
    }

    @Override
    public List<PostVO> getPostCollect(int curPage, int pageSize, Long userId) {
        int offset = 0;
        if ((curPage - 1) * pageSize > 0) {
            offset = (curPage - 1) * pageSize;
        }
        List<PostVO> postVOS = postMapper.selectPostCollect(offset, pageSize, userId);
        return postVOS;
    }

    @Override
    public List<PostVO> getPostActions(GetPostActionsREQ req) {
        int curPage = req.getCurPage();
        int pageSize = req.getPageSize();
        int offset = 0;
        if ((curPage - 1) * pageSize > 0) {
            offset = (curPage - 1) * pageSize;
        }
        req.setOffset(offset);
        req.setUserId(ThreadLocalUtils.get().getUserId());
        List<PostVO> postVOS = postMapper.getPostActions(req);
        return postVOS;
    }

    @Override
    public List<PostVO> listPost(SysPostDto postDto) {
        List<PostVO> postVOS = postMapper.listPost(postDto);
        //获取标签，转换为map
        Map<Long, String> tagMap = tagService.list().stream().collect(Collectors.toMap(Tag::getId, Tag::getTagName));
        //标签转化为名字
        postVOS.forEach(item -> {
            String tagsStr = item.getTags();
            Gson gson = new Gson();
            Long[] tags = gson.fromJson(tagsStr, Long[].class);
            if (tags != null) {
                ArrayList<String> tagsName = new ArrayList<>();
                Arrays.stream(tags).forEach(index -> {
                    tagsName.add(tagMap.get(index));
                });
                item.setTagsName(tagsName);
            }
        });
        return postVOS;
    }

    @Override
    public Boolean checkPost(CheckPostDto checkPostDto) {
        Post post = new Post();
        post.setId(checkPostDto.getPostId());
        post.setReviewStatus(checkPostDto.getReviewStatus());
        post.setReviewMessage(checkPostDto.getReviewMessage());
        return this.updateById(post);
    }

    @Override
    public Boolean recheck(Long id) {
        return this.lambdaUpdate().eq(Post::getId,id).set(Post::getReviewStatus, 0).update();
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




