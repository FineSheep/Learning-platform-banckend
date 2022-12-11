package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.User;
import fun.haoyang666.www.service.UserService;
import fun.haoyang666.www.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author yang
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2022-12-10 16:20:50
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




