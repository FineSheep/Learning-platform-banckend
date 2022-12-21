package fun.haoyang666.www.service;

import fun.haoyang666.www.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author yang
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2022-12-10 16:20:50
*/
public interface UserService extends IService<User> {

    Long userRegister(String email, String password, String userCode);

    void getCode(String email);
}
