package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.entity.Post;
import fun.haoyang666.www.service.PostService;
import fun.haoyang666.www.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
* @author yang
* @description 针对表【post(帖子)】的数据库操作Service实现
* @createDate 2022-12-10 16:20:50
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService {

}




