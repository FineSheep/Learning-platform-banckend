package fun.haoyang666.www.service.impl;

import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.domain.vo.LeaderVo;
import fun.haoyang666.www.service.LeaderService;
import fun.haoyang666.www.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LeaderServiceImpl implements LeaderService {

    @Resource
    private RedisTemplate<String, LeaderVo> redisTemplate;

    @Resource
    private UserService userService;

    @Override
    public Map<String, List<LeaderVo>> leaderBorder() {
        List<LeaderVo> day = dayLeader();
        List<LeaderVo> total = totalLeader();
        HashMap<String, List<LeaderVo>> map = new HashMap<>();
        map.put(Constant.DAY_LEADER, day);
        map.put(Constant.TOTAL_LEADER, total);
        return map;
    }

    private List<LeaderVo> dayLeader() {
        ZSetOperations<String, LeaderVo> zSet = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<LeaderVo>> typedTuples = zSet.reverseRangeWithScores(Constant.REDIS_DAY_LEADER, 0, 4);
        if (typedTuples == null || typedTuples.size() == 0) {
            return new ArrayList<>();
        }
        List<LeaderVo> list = typedTuples.stream().map(item -> {
            double score = item.getScore();
            item.getValue().setCorrectNum((int) score);
            return item.getValue();
        }).collect(Collectors.toList());
        return list;
    }

    private List<LeaderVo> totalLeader() {
        ZSetOperations<String, LeaderVo> zSet = redisTemplate.opsForZSet();
        Set<LeaderVo> range = zSet.reverseRange(Constant.REDIS_TOTAL_LEADER, 0, 4);
        if (range == null || range.size() == 0) {
            setTotalLeader();
            range = zSet.reverseRange(Constant.REDIS_TOTAL_LEADER, 0, 4);

        }
        ArrayList<LeaderVo> list = new ArrayList<>(range);
        return list;
    }

    /**
     * 获取总榜
     */
    public void setTotalLeader() {
        ZSetOperations<String, LeaderVo> zSet = redisTemplate.opsForZSet();
        zSet.removeRange(Constant.REDIS_TOTAL_LEADER, 0, -1);
        List<LeaderVo> vos = userService.lambdaQuery().orderByDesc(User::getCorrectNum).last("limit 5").list()
                .stream().map(this::convert).collect(Collectors.toList());
        vos.forEach(item -> {
            zSet.add(Constant.REDIS_TOTAL_LEADER, item, item.getCorrectNum());
        });

    }

    private LeaderVo convert(User user) {
        LeaderVo vo = new LeaderVo();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
