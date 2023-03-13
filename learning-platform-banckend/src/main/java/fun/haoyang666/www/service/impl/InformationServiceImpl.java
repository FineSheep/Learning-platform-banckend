package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.admin.dto.InfoParamDto;
import fun.haoyang666.www.domain.entity.Information;
import fun.haoyang666.www.mapper.InformationMapper;
import fun.haoyang666.www.service.InformationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yang
 * @description 针对表【information】的数据库操作Service实现
 * @createDate 2022-12-31 16:29:03
 */
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information>
        implements InformationService {

    @Override
    public List<Information> getInformationPage(int curPage, int pageSize) {
        LambdaQueryWrapper<Information> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Information::getPutTime);
        queryWrapper.eq(Information::getType, 0);
        Page<Information> page = this.page(new Page<>(curPage, pageSize), queryWrapper);
        return page.getRecords();
    }

    @Override
    public Information getNewById(Long id) {
        return this.getById(id);
    }

    @Override
    public List<Information> listInfo(InfoParamDto infoParamDto) {
        Long id = infoParamDto.getId();
        String title = infoParamDto.getTitle();
        Integer type = infoParamDto.getType();
        LocalDateTime startTime = infoParamDto.getStartTime();
        LocalDateTime endTime = infoParamDto.getEndTime();
        String source = infoParamDto.getSource();
        LambdaQueryWrapper<Information> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, Information::getId, id);
        queryWrapper.like(StringUtils.isNotBlank(source), Information::getSource, source);
        queryWrapper.like(StringUtils.isNotBlank(title), Information::getTitle, title);
        queryWrapper.eq(type != null, Information::getType, type);
        queryWrapper.gt(startTime != null, Information::getPutTime, startTime);
        queryWrapper.lt(endTime != null, Information::getPutTime, endTime);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean changeInfo(Long id, Integer type) {
        return this.lambdaUpdate().eq(Information::getId, id).set(Information::getType, type).update();
    }
}




