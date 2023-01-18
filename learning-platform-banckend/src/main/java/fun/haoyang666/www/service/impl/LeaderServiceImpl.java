package fun.haoyang666.www.service.impl;

import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.domain.vo.LeaderBorderVo;
import fun.haoyang666.www.service.LeaderService;
import fun.haoyang666.www.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yang
 * @createTime 2023/1/18 12:44
 * @description
 */
@Service
public class LeaderServiceImpl implements LeaderService {

    @Resource
    private RedisTemplate<String, LeaderBorderVo> redisTemplate;

    @Resource
    private UserService userService;

    @Override
    public Map<String, List<LeaderBorderVo>> leaderBorder() {
        List<LeaderBorderVo> day = dayLeader();
        List<LeaderBorderVo> total = totalLeader();
        HashMap<String, List<LeaderBorderVo>> map = new HashMap<>();
        map.put(Constant.DAY_LEADER, day);
        map.put(Constant.TOTAL_LEADER, total);
        return map;
    }

    private List<LeaderBorderVo> dayLeader() {
        ZSetOperations<String, LeaderBorderVo> zSet = redisTemplate.opsForZSet();
        Set<LeaderBorderVo> range = zSet.range(Constant.DAY_LEADER, 0, 4);
        if (range == null) {
            return new ArrayList<>();
        }
        ArrayList<LeaderBorderVo> list = new ArrayList<>(range);
        Collections.reverse(list);
        return list;
    }

    private List<LeaderBorderVo> totalLeader() {
        ZSetOperations<String, LeaderBorderVo> zSet = redisTemplate.opsForZSet();
        Set<LeaderBorderVo> range = zSet.range(Constant.TOTAL_LEADER, 0, 4);
        if (range == null) {
            setTotalLeader();
        }
        ArrayList<LeaderBorderVo> list = new ArrayList<>(range);
        Collections.reverse(list);
        return list;
    }

    /**
     * 获取总榜
     */
    public void setTotalLeader() {
        ZSetOperations<String, LeaderBorderVo> zSet = redisTemplate.opsForZSet();
        zSet.removeRange(Constant.TOTAL_LEADER, 0, -1);
        List<LeaderBorderVo> vos = userService.lambdaQuery().orderByDesc(User::getCorrectNum).last("limit 5").list()
                .stream().map(this::convert).collect(Collectors.toList());
        vos.forEach(item -> {
            zSet.add(Constant.TOTAL_LEADER, item, item.getCorrectNum());
        });

    }

    private LeaderBorderVo convert(User user) {
        LeaderBorderVo vo = new LeaderBorderVo();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
