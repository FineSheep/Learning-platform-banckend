package fun.haoyang666.www.common.enums;

import fun.haoyang666.www.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户状态
 * @author yang
 */
@Slf4j
public enum StatusEnum {

    /**
     * 待匹配
     */
    IDLE,
    /**
     * 匹配中
     */
    IN_MATCH,
    /**
     * 游戏中
     */
    IN_GAME,
    /**
     * 游戏结束
     */
    GAME_OVER,
    ;

    public static StatusEnum getStatusEnum(String status) {
        switch (status) {
            case "IDLE":
                return IDLE;
            case "IN_MATCH":
                return IN_MATCH;
            case "IN_GAME":
                return IN_GAME;
            case "GAME_OVER":
                return GAME_OVER;
            default:
               throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    public String getValue() {
        return this.name();
    }
}
