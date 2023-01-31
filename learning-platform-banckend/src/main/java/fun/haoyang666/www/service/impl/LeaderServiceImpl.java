package fun.haoyang666.www.service.impl;

import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.domain.vo.LeaderVO;
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
    private RedisTemplate<String, LeaderVO> redisTemplate;

    @Resource
    private UserService userService;

    @Override
    public Map<String, List<LeaderVO>> leaderBorder() {
        List<LeaderVO> day = dayLeader();
        List<LeaderVO> total = totalLeader();
        HashMap<String, List<LeaderVO>> map = new HashMap<>();
        map.put(Constant.DAY_LEADER, day);
        map.put(Constant.TOTAL_LEADER, total);
        return map;
    }

    private List<LeaderVO> dayLeader() {
        ZSetOperations<String, LeaderVO> zSet = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<LeaderVO>> typedTuples = zSet.reverseRangeWithScores(Constant.REDIS_DAY_LEADER, 0, 4);
        if (typedTuples == null || typedTuples.size() == 0) {
            return new ArrayList<>();
        }
        List<LeaderVO> list = typedTuples.stream().map(item -> {
            double score = item.getScore();
            item.getValue().setCorrectNum((int) score);
            return item.getValue();
        }).collect(Collectors.toList());
        return list;
    }

    private List<LeaderVO> totalLeader() {
        ZSetOperations<String, LeaderVO> zSet = redisTemplate.opsForZSet();
        Set<LeaderVO> range = zSet.reverseRange(Constant.REDIS_TOTAL_LEADER, 0, 4);
        if (range == null || range.size() == 0) {
            setTotalLeader();
            range = zSet.reverseRange(Constant.REDIS_TOTAL_LEADER, 0, 4);

        }
        ArrayList<LeaderVO> list = new ArrayList<>(range);
        return list;
    }

    /**
     * 获取总榜
     */
    public void setTotalLeader() {
        ZSetOperations<String, LeaderVO> zSet = redisTemplate.opsForZSet();
        zSet.removeRange(Constant.REDIS_TOTAL_LEADER, 0, -1);
        List<LeaderVO> vos = userService.lambdaQuery().orderByDesc(User::getCorrectNum).last("limit 5").list()
                .stream().map(this::convert).collect(Collectors.toList());
        vos.forEach(item -> {
            zSet.add(Constant.REDIS_TOTAL_LEADER, item, item.getCorrectNum());
        });

    }

    private LeaderVO convert(User user) {
        LeaderVO vo = new LeaderVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
