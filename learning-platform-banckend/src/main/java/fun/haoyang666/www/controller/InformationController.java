package fun.haoyang666.www.controller;

import com.google.gson.Gson;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.entity.Information;
import fun.haoyang666.www.domain.entity.Post;
import fun.haoyang666.www.domain.req.PageREQ;
import fun.haoyang666.www.domain.vo.SpiderVO;
import fun.haoyang666.www.service.InformationService;
import fun.haoyang666.www.service.PostService;
import fun.haoyang666.www.utils.ResultUtils;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yang
 * @createTime 2022/12/31 17:05
 * @description
 */
@RestController
@RequestMapping("information")
public class InformationController {
    @Resource
    private InformationService informationService;
    @Resource
    private PostService postService;

    @GetMapping("getInfo")
    public BaseResponse getInfo(PageREQ pageReq) {
        if (pageReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        List<Information> dto = informationService.getInformationPage(pageReq.getCurPage(), pageReq.getPageSize());
        return ResultUtils.success(dto);
    }

    @GetMapping("getNewById")
    public BaseResponse getNewById(Long id) {
        Information info = informationService.getNewById(id);
        return ResultUtils.success(info);
    }

    @GetMapping("hotAndNew")
    public BaseResponse hotAndNew() {
        List<Information> infos = informationService.lambdaQuery()
                .orderByDesc(Information::getPutTime)
                .last("limit 10").list();
        List<Post> post = postService.lambdaQuery()
                .orderByDesc(Post::getThumbNum)
                .last("limit 10").list();
        HashMap<String, List> map = new HashMap<>();
        map.put("info", infos);
        map.put("post", post);
        return ResultUtils.success(map);
    }

}
