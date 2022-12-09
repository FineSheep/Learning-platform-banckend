package com.haoyang666.fun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoyang666.fun.domain.User;
import com.haoyang666.fun.service.UserService;
import com.haoyang666.fun.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author yang
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2022-12-09 19:29:55
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




