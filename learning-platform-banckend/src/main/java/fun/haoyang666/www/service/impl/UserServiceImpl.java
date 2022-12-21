package fun.haoyang666.www.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.UserService;
import fun.haoyang666.www.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

import static fun.haoyang666.www.common.Constant.SALTY;
import static fun.haoyang666.www.common.Constant.USER_CODE;

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

    @Override
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
        this.save(saveUser);
        return user.getId();
    }

    @Override
    public void getCode(String email) {
        int code = RandomUtil.randomInt();
        String content = "您的验证码为" + code + ",5分钟内有效！";
        new Thread(() -> {
            try {
                MailUtil.send(email, "验证码", content, false);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱错误");
            }
        }).start();
        redisTemplate.opsForValue().set(email, String.valueOf(code), 5, TimeUnit.MINUTES);
    }
}




