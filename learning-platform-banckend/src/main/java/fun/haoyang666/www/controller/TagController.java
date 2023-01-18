package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.domain.entity.Tag;
import fun.haoyang666.www.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yang
 * @createTime 2023/1/14 15:25
 * @description
 */
@RestController
@RequestMapping("tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("getTags")
    public BaseResponse<List<Tag>> getTags() {
        List<Tag> list = tagService.list();
        return ResultUtils.success(list);
    }
}
