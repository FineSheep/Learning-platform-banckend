package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.dto.InformationDto;
import fun.haoyang666.www.domain.entity.Information;
import fun.haoyang666.www.service.InformationService;
import fun.haoyang666.www.mapper.InformationMapper;
import org.springframework.stereotype.Service;

/**
* @author yang
* @description 针对表【information】的数据库操作Service实现
* @createDate 2022-12-31 16:29:03
*/
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information>
    implements InformationService{

    @Override
    public InformationDto getInformationPage(int curPage, int pageSize) {
        LambdaQueryWrapper<Information> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Information::getPutTime);
        Page<Information> page = this.page(new Page<>(curPage, pageSize),queryWrapper);
        long count = this.count();
        InformationDto dto = new InformationDto();
        dto.setTotal(count);
        dto.setRecords(page.getRecords());
        return dto;
    }
}




