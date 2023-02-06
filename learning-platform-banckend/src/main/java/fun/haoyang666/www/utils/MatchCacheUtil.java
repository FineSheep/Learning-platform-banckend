package fun.haoyang666.www.utils;

import fun.haoyang666.www.common.enums.EnumRedisKey;
import fun.haoyang666.www.common.enums.StatusEnum;
import fun.haoyang666.www.socket.MatchSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author yang
 */
@Component
@Slf4j
public class MatchCacheUtil {

    /**
     * 用户 userId 为 key，MatchSocket 为 value
     */
    private static final Map<String, MatchSocket> CLIENTS = new HashMap<>();

    /**
     * key 是标识存储用户在线状态的 EnumRedisKey，value 为 map 类型，其中用户 userId 为 key，用户在线状态 为 value
     */
    @Resource
    private RedisTemplate<String, Map<String, String>> redisTemplate;

    /**
     * 添加客户端
     */
    public void addClient(String userId, MatchSocket websocket) {
        CLIENTS.put(userId, websocket);
    }

    /**
     * 移除客户端
     */
    public void removeClient(String userId) {
        CLIENTS.remove(userId);
    }

    /**
     * 获取客户端
     */
    public MatchSocket getClient(String userId) {
        return CLIENTS.get(userId);
    }

    /**
     * 移除用户在线状态
     */
    public void removeUserOnlineStatus(String userId) {
        log.info("user:{}", userId);
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();

        redisTemplate.opsForHash().delete(EnumRedisKey.USER_STATUS.getKey(), userId.toString());
    }

    /**
     * 获取用户在线状态
     */
    public StatusEnum getUserOnlineStatus(String userId) {
        Object status = redisTemplate.opsForHash().get(EnumRedisKey.USER_STATUS.getKey(), userId);
        if (status == null) {
            return null;
        }
        return StatusEnum.getStatusEnum(status.toString());
    }

    /**
     * 设置用户为 IDLE 状态
     */
    public void setUserIDLE(String userId) {
        removeUserOnlineStatus(userId);
        redisTemplate.opsForHash().put(EnumRedisKey.USER_STATUS.getKey(), userId, StatusEnum.IDLE.getValue());

    }

    /**
     * 设置用户为 IN_MATCH 状态
     */
    public void setUserInMatch(String userId) {
        removeUserOnlineStatus(userId);
        redisTemplate.opsForHash().put(EnumRedisKey.USER_STATUS.getKey(), userId, StatusEnum.IN_MATCH.getValue());
    }

    /**
     * 随机获取处于匹配状态的用户（除了指定用户外）
     */
    public String getUserInMatchRandom(String userId) {
        Optional<Map.Entry<Object, Object>> any = redisTemplate.opsForHash().entries(EnumRedisKey.USER_STATUS.getKey())
                .entrySet().stream().filter(entry -> entry.getValue().equals(StatusEnum.IN_MATCH.getValue()) && !entry.getKey().equals(userId))
                .findAny();
        return any.map(entry -> entry.getKey().toString()).orElse(null);
    }

    /**
     * 设置用户为 IN_GAME 状态
     */
    public void setUserInGame(String userId) {
        removeUserOnlineStatus(userId);
        redisTemplate.opsForHash().put(EnumRedisKey.USER_STATUS.getKey(), userId, StatusEnum.IN_GAME.getValue());
    }

    /**
     * 用户处于同一房间
     *
     * @param userId
     * @param receiverId
     */
    public void setUserInRoom(String userId, String receiverId) {
        redisTemplate.opsForHash().put(EnumRedisKey.ROOM.toString(), userId, receiverId);
        redisTemplate.opsForHash().put(EnumRedisKey.ROOM.toString(), receiverId, userId);
    }

    /**
     * 移除对战房间
     *
     * @param userId
     */
    public void removeRoom(String userId) {
        redisTemplate.opsForHash().delete(EnumRedisKey.ROOM.toString(), userId);
    }

    /**
     * 查询对手是否在游戏中。。。
     *
     * @param id
     * @return
     */
    public boolean getUserRoom(String id) {
        return redisTemplate.opsForHash().get(EnumRedisKey.ROOM.toString(), id) != null;
    }

    /**
     * 设置处于游戏中的用户的对战信息
     */
    public void setUserMatchInfo(String userId, Object userMatchInfo) {
        redisTemplate.opsForHash().put(EnumRedisKey.USER_MATCH_INFO.getKey(), userId, userMatchInfo);
    }

    /**
     * 移除处于游戏中的用户的对战信息
     */
    public void removeUserMatchInfo(String userId) {
        redisTemplate.opsForHash().delete(EnumRedisKey.USER_MATCH_INFO.getKey(), userId);
    }

    /**
     * 获取处于游戏中的用户的对战信息
     */
    public Object getUserMatchInfo(String userId) {
        return redisTemplate.opsForHash().get(EnumRedisKey.USER_MATCH_INFO.getKey(), userId);
    }

    /**
     * 设置用户为游戏结束状态
     */
    public synchronized void setUserGameOver(String userId) {
        removeUserOnlineStatus(userId);
        redisTemplate.opsForHash().put(EnumRedisKey.USER_STATUS.getKey(), userId, StatusEnum.GAME_OVER.getValue());
    }
}
