package fun.haoyang666.www.socket;

import com.google.gson.Gson;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.MessageTypeEnum;
import fun.haoyang666.www.domain.req.PKREQ;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.impl.MatchImpl;
import fun.haoyang666.www.utils.MatchCacheUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;

/**
 * @author yang
 * @createTime 2022/12/12 19:01
 * @description
 */
@Component
@Slf4j
@ServerEndpoint(value = "/game/match/{userId}")
@Data
public class MatchSocket {

    private Session session;

    private String userId;
    private String opponent;

    private static MatchCacheUtil matchCacheUtil;
    private static MatchImpl matchImpl;

    @Autowired
    public void setMatchImpl(MatchImpl matchImpl) {
        MatchSocket.matchImpl = matchImpl;
    }

    @Autowired
    public void setMatchCacheUtil(MatchCacheUtil matchCacheUtil) {
        MatchSocket.matchCacheUtil = matchCacheUtil;
    }


    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        log.info("ChatWebsocket open 有新连接加入 userId: {}", userId);
        this.userId = userId;
        this.session = session;
        matchCacheUtil.addClient(userId, this);
//        matchCacheUtil.setUserIDLE(userId);
        log.info("ChatWebsocket open 连接建立完成 userId: {}", userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("ChatWebsocket onError 发生了错误 userId: {}, errorMessage: {}", userId, error.getMessage());
        log.info("ChatWebsocket onError 连接断开完成 userId: {}", userId);
    }

    @OnClose
    public void onClose() {
        log.info("ChatWebsocket onClose 连接断开 userId: {}", userId);
        //移除客户端
        matchCacheUtil.removeClient(userId);
        //移除在线状态
        matchCacheUtil.removeUserOnlineStatus(userId);
        //移除对战室状态
        matchCacheUtil.removeRoom(userId);
        //移除游戏中的用户的对战信息
        matchCacheUtil.removeUserMatchInfo(userId);
        //记录数据库
        //------
        log.info("对手：{}", opponent);
        log.info("ChatWebsocket onClose 连接断开完成 userId: {}", userId);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("ChatWebsocket onMessage userId: {}, 来自客户端的消息 message: {}", userId, message);
        Gson gson = new Gson();
        PKREQ PKREQ = gson.fromJson(message, PKREQ.class);
        MessageTypeEnum type = PKREQ.getType();
        log.info("ChatWebsocket onMessage userId: {}, 来自客户端的消息类型 type: {}", userId, type);
        if (type == MessageTypeEnum.PING) {
            log.info("websocket心跳检测：{}", LocalDateTime.now());
        } else if (type == MessageTypeEnum.MATCH_USER) {
            matchImpl.match(userId);
        } else if (type == MessageTypeEnum.CANCEL_MATCH) {
            matchImpl.cancel(userId);
        } else if (type == MessageTypeEnum.PLAY_GAME) {
            opponent = PKREQ.getOpponent();
        } else if (type == MessageTypeEnum.GAME_OVER) {
            matchImpl.gameOver(PKREQ);
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        log.info("ChatWebsocket onMessage userId: {} 消息接收结束", userId);
    }

}
