package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.entity.Tag;
import fun.haoyang666.www.mapper.TagMapper;
import fun.haoyang666.www.service.TagService;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @createTime 2023/1/14 15:13
 * @description
 */

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
