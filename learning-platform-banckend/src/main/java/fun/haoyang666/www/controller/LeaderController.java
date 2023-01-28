package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.domain.vo.LeaderVo;
import fun.haoyang666.www.service.LeaderService;
import fun.haoyang666.www.utils.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yang
 * @createTime 2023/1/18 12:44
 * @description
 */
@RestController
@RequestMapping("leader")
public class LeaderController {
    @Resource
    private LeaderService leaderService;

    @GetMapping("getLeader")
    public BaseResponse getLeader() {
        Map<String, List<LeaderVo>> leader = leaderService.leaderBorder();
        return ResultUtils.success(leader);
    }
}
