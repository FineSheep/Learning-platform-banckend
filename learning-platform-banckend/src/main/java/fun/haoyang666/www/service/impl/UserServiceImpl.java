package fun.haoyang666.www.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.dto.UserDto;
import fun.haoyang666.www.domain.dto.UserInfoDto;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.domain.vo.UserVo;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.UserService;
import fun.haoyang666.www.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static fun.haoyang666.www.common.Constant.*;

/**
 * @author yang
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2022-12-10 16:20:50
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

/*    @Override
    public Long userRegister(String email, String password, String userCode) {
        String sysCode = redisTemplate.opsForValue().get(email);
        if (!userCode.equals(sysCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }
        //1.校验
        //  1.1 email是否重复
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getEmail, email);
        User user = this.getOne(query);
        if (user != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能重复注册");
        }
        //  1.2 密码长度是否符合要求   6-10
        if (password.length() < 6 || password.length() > 10) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不符合要求");
        }
        //2.密码加密
        String safePass = DigestUtils.md5DigestAsHex((SALTY + password).getBytes());
        //3.数据插入
        User saveUser = new User();
        saveUser.setEmail(email);
        saveUser.setUserPassword(safePass);
        saveUser.setId(RandomUtil.randomLong());
        this.save(saveUser);
        return saveUser.getId();
    }*/

    @Override
    public void getCode(String email) {
        int code = Math.abs(RandomUtil.randomInt());
        String content = "您的验证码为" + code + ",5分钟内有效！";
        new Thread(() -> {
            try {
                MailUtil.send(email, "验证码", content, false);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱错误");
            }
        }).start();
        redisTemplate.opsForValue().set("code:" + email, String.valueOf(code), 5, TimeUnit.MINUTES);
    }

    @Override
    public UserDto userLogin(String email, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未注册");
        }
//        String safePass = DigestUtils.md5DigestAsHex((SALTY + password).getBytes());
        String safePass = password;
        if (safePass.equals(user.getUserPassword())) {
            UserDto safeUser = new UserDto();
            BeanUtils.copyProperties(user, safeUser);
            return safeUser;
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

    }

    @Override
    public UserDto loginOrRegister(String email, String code) {
        String redisCode = redisTemplate.opsForValue().get("code:" + email);
        if (!code.equals(redisCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误或失效");
        }
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getEmail, email);
        User user = this.getOne(query);
        UserDto userDto = new UserDto();
        if (user == null) {
            //用户没有注册
            //开始注册
            //数据插入
            User insertUser = new User();
            insertUser.setEmail(email);
            insertUser.setUsername(RandomUtil.randomString(10));
            insertUser.setAvatarUrl(DEFAULT_AVATAR);
            insertUser.setCreateTime(LocalDateTime.now());
            this.save(insertUser);
            BeanUtils.copyProperties(insertUser, userDto);
            return userDto;
        } else {
            //已注册
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        }
    }

    @Override
    public UserInfoDto userInfo(Long userId) {
        User user = getUserById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查无此人");
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(user, userInfoDto);
        return userInfoDto;
    }
/*
    @Override
    public UserDto loginByCode(String email, String code) {
        return null;
    }*/

    private User getUserById(Long id) {
        User user = this.lambdaQuery().eq(User::getId, id).one();
        if (user == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查无此人");
        }
        return user;
    }
}




