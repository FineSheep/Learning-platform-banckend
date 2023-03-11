package fun.haoyang666.www.service;

import fun.haoyang666.www.admin.UserParamReq;
import fun.haoyang666.www.admin.dto.SysUserDto;
import fun.haoyang666.www.domain.dto.UserDTO;
import fun.haoyang666.www.domain.dto.UserInfoDTO;
import fun.haoyang666.www.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.haoyang666.www.domain.req.UpdatePasswordREQ;
import fun.haoyang666.www.domain.req.UserInfoREQ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author yang
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2022-12-10 16:20:50
 */
public interface UserService extends IService<User> {

//    Long userRegister(String email, String password, String userCode);

    void getCode(String email);


    UserDTO userLogin(String email, String password,HttpServletRequest request, HttpServletResponse response);

//    UserDto loginByCode(String email, String code);

    UserDTO loginOrRegister(String email, String code,HttpServletRequest request, HttpServletResponse response);

    UserInfoDTO userInfo(Long userId);


    int updateScore(long userId, long num);

    void updateUserInfo(UserInfoREQ req);

    boolean updatePassword(UpdatePasswordREQ updatePasswordREQ);

    void getCodeById(Long userId);

    List<SysUserDto> listSysUser(UserParamReq req);

    Integer forbiddenUser(Long userId, Integer status);

    Boolean removeUser(Long userId);

    Boolean resetUser(Long userId);

    Boolean updateUser(SysUserDto dto);
}
