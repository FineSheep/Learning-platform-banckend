package fun.example.learningplatformbanckend;

import fun.haoyang666.www.LearnPlatApplication;
import fun.haoyang666.www.domain.dto.MessageCountDTO;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.mapper.MessageUserMapper;
import fun.haoyang666.www.service.MessageService;
import fun.haoyang666.www.service.MessageUserService;
import fun.haoyang666.www.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yang
 * @createTime 2023/2/17 10:45
 * @description
 */
@SpringBootTest(classes = LearnPlatApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BootTest {
    @Autowired
    private MessageUserMapper mapper;
    @Autowired
    private MessageService service;

    @Resource
    private UserService userService;
    @Test
    void test1(){
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        userService.updateById(user);
    }

    @Test
    void  test2(){
        MessageCountDTO messageCountDTO = service.dotMessageAll(1000L);
        System.out.println(messageCountDTO);
    }
}
