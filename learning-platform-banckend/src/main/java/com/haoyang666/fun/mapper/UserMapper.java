package com.haoyang666.fun.mapper;

import com.haoyang666.fun.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yang
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2022-12-09 19:29:55
* @Entity com.haoyang666.fun.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




