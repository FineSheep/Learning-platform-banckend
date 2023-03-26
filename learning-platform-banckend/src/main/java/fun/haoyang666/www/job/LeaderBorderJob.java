package fun.haoyang666.www.job;

import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.domain.vo.LeaderVO;
import fun.haoyang666.www.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yang
 * @createTime 2023/1/18 9:30
 * @description
 */
@Component
@Slf4j
public class LeaderBorderJob {

    @Resource
    private RedisTemplate<String, LeaderVO> redisTemplate;

    @Resource
    private UserService userService;

    @Scheduled(cron = "0 0 4 * * ? ")
    public void leaderBorder() {
        cleaDayLeader();
        totalLeader();
    }

    /**
     * 移除日榜，日榜实时刷新
     */
    public void cleaDayLeader() {
        ZSetOperations<String, LeaderVO> zSet = redisTemplate.opsForZSet();
        zSet.removeRange(Constant.REDIS_DAY_LEADER, 0, -1);
    }

    /**
     * 获取总榜
     */
    public void totalLeader() {
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
