package fun.haoyang666.www.service;

import fun.haoyang666.www.domain.dto.UserDto;
import fun.haoyang666.www.domain.dto.UserInfoDto;
import fun.haoyang666.www.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yang
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2022-12-10 16:20:50
 */
public interface UserService extends IService<User> {

//    Long userRegister(String email, String password, String userCode);

    void getCode(String email);


    UserDto userLogin(String email, String password);

//    UserDto loginByCode(String email, String code);

    UserDto loginOrRegister(String email, String code);

    UserInfoDto userInfo(Long userId);


    int updateScore(long userId, long num);
}
