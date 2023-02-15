package fun.haoyang666.www.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.haoyang666.www.domain.entity.Records;
import fun.haoyang666.www.domain.vo.RecordVO;

import java.util.List;

/**
 * @author yang
 * @description 针对表【records(用户做题记录)】的数据库操作Service
 * @createDate 2023-01-08 10:55:38
 */
public interface RecordsService extends IService<Records> {

    List<RecordVO> getRecordsByUid(long uid, int curPage, int pageSize);

    long saveRecord(long userId, long time, long sum, long correct, Long opponent, Boolean result);


    List<RecordVO> getPKRecords(long uid);
}
