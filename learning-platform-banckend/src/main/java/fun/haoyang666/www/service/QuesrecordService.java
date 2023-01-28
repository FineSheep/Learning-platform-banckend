package fun.haoyang666.www.service;

import fun.haoyang666.www.domain.entity.Quesrecord;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.haoyang666.www.domain.vo.GradeVo;

import java.util.List;

/**
* @author yang
* @description 针对表【quesrecord(题目记录关联)】的数据库操作Service
* @createDate 2023-01-07 14:00:56
*/
public interface QuesrecordService extends IService<Quesrecord> {

    void saveRecordQues(long recordId, long userId, List<GradeVo> correctList, List<GradeVo> falseList);
}
