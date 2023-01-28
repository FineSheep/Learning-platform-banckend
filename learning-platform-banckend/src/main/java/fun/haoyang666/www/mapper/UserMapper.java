package fun.haoyang666.www.mapper;

import fun.haoyang666.www.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yang
 * @description 针对表【user(用户)】的数据库操作Mapper
 * @createDate 2022-12-10 16:20:50
 * @Entity com.haoyang666.fun.domain.User
 */
public interface UserMapper extends BaseMapper<User> {

    int updateCorrectNum( Long userId,long num);
}




