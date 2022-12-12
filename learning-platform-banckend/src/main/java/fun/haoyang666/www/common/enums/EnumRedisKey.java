package fun.haoyang666.www.common.enums;

/**
 * @author yang
 */
public enum EnumRedisKey {

    /**
     * userOnline 在线状态
     */
    USER_STATUS,
    /**
     * userOnline 对局信息
     */
    USER_IN_PLAY,
    /**
     * userOnline 匹配信息
     */
    USER_MATCH_INFO,
    /**
     * 房间
     */
    ROOM;

    public String getKey() {
        return this.name();
    }
}
