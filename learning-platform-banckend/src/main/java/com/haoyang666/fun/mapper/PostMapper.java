package com.haoyang666.fun.mapper;

import com.haoyang666.fun.domain.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yang
* @description 针对表【post(帖子)】的数据库操作Mapper
* @createDate 2022-12-09 19:29:55
* @Entity com.haoyang666.fun.domain.Post
*/
@Mapper
public interface PostMapper extends BaseMapper<Post> {

}




