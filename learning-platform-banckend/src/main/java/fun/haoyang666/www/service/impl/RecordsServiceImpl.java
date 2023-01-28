package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.dto.ScrollerDto;
import fun.haoyang666.www.domain.entity.Records;
import fun.haoyang666.www.domain.vo.GradeVo;
import fun.haoyang666.www.domain.vo.RecordVo;
import fun.haoyang666.www.service.RecordsService;
import fun.haoyang666.www.mapper.RecordsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yang
 * @description 针对表【records(用户做题记录)】的数据库操作Service实现
 * @createDate 2023-01-08 10:55:38
 */
@Service
@Slf4j
public class RecordsServiceImpl extends ServiceImpl<RecordsMapper, Records>
        implements RecordsService {
    @Resource
    private RecordsMapper recordsMapper;

    @Override
    public ScrollerDto<RecordVo> getRecordsByUid(long uid, int curPage, int pageSize) {
        LambdaQueryWrapper<Records> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Records::getUserId, uid);
        Page<Records> page = this.page(new Page<>(curPage, pageSize), queryWrapper);
        long pages = page.getPages();
        ScrollerDto<RecordVo> dto = new ScrollerDto<>();
        if (curPage < pages) {
            dto.setHasNext(true);
        } else {
            dto.setHasNext(false);
        }
        List<RecordVo> collect = page.getRecords().stream().map(this::toRecordVo).collect(Collectors.toList());
        dto.setRecords(collect);
        return dto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public long saveRecord(long userId, long time, long sum, long correct) {
        Records records = new Records();
        records.setUserId(userId);
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusSeconds(time);
        records.setSum(sum);
        records.setStartTime(startTime);
        records.setCurrectSum(correct);
        records.setEndTime(endTime);
        this.save(records);
        log.info("record:{}", records);
        return records.getId();
    }



    /**
     * 转换结构
     *
     * @param records 被转换记录
     * @return 转换之后记录
     */
    private RecordVo toRecordVo(Records records) {
        RecordVo recordVo = new RecordVo();
        BeanUtils.copyProperties(records, recordVo);
        recordVo.setAnswerTime(records.getStartTime(), records.getEndTime());
        return recordVo;
    }
}




