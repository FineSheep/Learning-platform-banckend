package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}




