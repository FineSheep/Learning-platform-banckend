package fun.haoyang666.www.common.enums;

/**
 * @author yang
 */
public enum EnumRedisKey {

    /**
     * 在线状态
     */
    USER_STATUS,
    /**
     * 对局信息
     */
    USER_IN_PLAY,
    /**
     * 房间信息
     */
    ROOM,
    /**
     * 匹配信息
     */
    USER_MATCH_INFO;

    public String getKey() {
        return this.name();
    }
}
