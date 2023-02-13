package fun.haoyang666.www.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.dto.UserDTO;
import fun.haoyang666.www.domain.dto.UserInfoDTO;
import fun.haoyang666.www.domain.entity.CollectPost;
import fun.haoyang666.www.domain.entity.Post;
import fun.haoyang666.www.domain.entity.ThumbPost;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.domain.req.UserInfoREQ;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.CollectPostService;
import fun.haoyang666.www.service.PostService;
import fun.haoyang666.www.service.ThumbPostService;
import fun.haoyang666.www.service.UserService;
import fun.haoyang666.www.mapper.UserMapper;
import fun.haoyang666.www.utils.ThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static fun.haoyang666.www.common.Constant.*;

/**
 * @author yang
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2022-12-10 16:20:50
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PostService postService;
    @Resource
    private ThumbPostService thumbPostService;
    @Resource
    private CollectPostService collectPostService;


    @Override
    public void getCode(String email) {
        int code = Math.abs(RandomUtil.randomInt());
        String content = "您的验证码为" + code + ",5分钟内有效！";
        ExecutorService threadPool = ThreadPool.instance();
        threadPool.execute(
                () -> {
                    try {
                        MailUtil.send(email, "验证码", content, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱错误");
                    }
                });
        redisTemplate.opsForValue().set("code:" + email, String.valueOf(code), 5, TimeUnit.MINUTES);
    }

    @Override
    public UserDTO userLogin(String email, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未注册");
        }
//        String safePass = DigestUtils.md5DigestAsHex((SALTY + password).getBytes());
        String safePass = password;
        if (safePass.equals(user.getUserPassword())) {
            UserDTO safeUser = new UserDTO();
            BeanUtils.copyProperties(user, safeUser);
            return safeUser;
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

    }

    @Override
    public UserDTO loginOrRegister(String email, String code) {
        String redisCode = redisTemplate.opsForValue().get("code:" + email);
        if (!code.equals(redisCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误或失效");
        }
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getEmail, email);
        User user = this.getOne(query);
        UserDTO userDto = new UserDTO();
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
    public UserInfoDTO userInfo(Long userId) {
        User user = getUserById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查无此人");
        }
        UserInfoDTO userInfoDto = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDto);
        Long postCount = postService.lambdaQuery().eq(Post::getUserId, userId).count();
        userInfoDto.setPostNum(postCount);
        Long thumbCount = thumbPostService.lambdaQuery().eq(ThumbPost::getUserId, userId).count();
        userInfoDto.setThumbNum(thumbCount);
        Long collectCount = collectPostService.lambdaQuery().eq(CollectPost::getUserId, userId).count();
        userInfoDto.setCollectNum(collectCount);
        return userInfoDto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public int updateScore(long userId, long num) {
        log.info("user:{}", userId);
        int i = userMapper.updateCorrectNum(userId, num);
        return i;
    }

    @Override
    public void updateUserInfo(UserInfoREQ req) {
        User user = new User();
        user.setId(req.getUserId());
        user.setUsername(req.getUsername());
        user.setGender(req.getGender());
        user.setProfile(req.getProfile());
        user.setPhone(req.getPhone());
        user.setBirthday(req.getBirthday());
        this.updateById(user);
    }

    private User getUserById(Long id) {
        User user = this.lambdaQuery().eq(User::getId, id).one();
        if (user == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查无此人");
        }
        return user;
    }
}




