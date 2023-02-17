package fun.haoyang666.www.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.haoyang666.www.domain.entity.MessageUser;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface MessageUserMapper extends BaseMapper<MessageUser> {

}
