package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.entity.Quesrecord;
import fun.haoyang666.www.mapper.QuesrecordMapper;
import fun.haoyang666.www.service.QuesrecordService;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @createTime 2023/1/17 11:44
 * @description
 */
@Service
public class QuesRecordServiceImpl extends ServiceImpl<QuesrecordMapper, Quesrecord>
        implements QuesrecordService {
}
