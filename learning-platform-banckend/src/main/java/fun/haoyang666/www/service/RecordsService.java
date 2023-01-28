package fun.haoyang666.www.service;

import fun.haoyang666.www.domain.dto.ScrollerDto;
import fun.haoyang666.www.domain.entity.Records;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.haoyang666.www.domain.vo.GradeVo;
import fun.haoyang666.www.domain.vo.RecordVo;

import java.util.List;

/**
* @author yang
* @description 针对表【records(用户做题记录)】的数据库操作Service
* @createDate 2023-01-08 10:55:38
*/
public interface RecordsService extends IService<Records> {

    ScrollerDto<RecordVo> getRecordsByUid(long uid, int curPage, int pageSize);

    long saveRecord(long userId,long time,long sum,long correct );


}
