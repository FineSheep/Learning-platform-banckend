package fun.haoyang666.www.factory;

import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.MessageTypeEnum;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.factory.strategy.*;

/**
 * @author yang
 * @createTime 2023/1/31 15:32
 * @description
 */

public class StrategyFactory {
    private StrategyFactory() {

    }

    public static Strategy createStrategy(MessageTypeEnum type) {
        switch (type) {
            case PING:
                return new PingStrategy();
            case MATCH_USER:
                return new MatchStrategy();
            case CANCEL_MATCH:
                return new CancelStrategy();
            case PLAY_GAME:
                return new PlayStrategy();
            case GAME_OVER:
                return new OverStrategy();
            default:
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);


        }
    }
}
