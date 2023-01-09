package fun.haoyang666.www.mapper;

import fun.haoyang666.www.domain.entity.Records;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author yang
* @description 针对表【records(用户做题记录)】的数据库操作Mapper
* @createDate 2023-01-08 10:55:38
* @Entity fun.haoyang666.www.domain.entity.Records
*/
public interface RecordsMapper extends BaseMapper<Records> {

//    List<Records> getRecordsByUId(long uid);

}




