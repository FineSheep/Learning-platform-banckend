package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.entity.MessageUser;
import fun.haoyang666.www.mapper.MessageUserMapper;
import fun.haoyang666.www.service.MessageUserService;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @createTime 2023/2/16 10:05
 * @description
 */
@Service
public class MessageUserServiceImpl extends ServiceImpl<MessageUserMapper, MessageUser> implements MessageUserService {
}
