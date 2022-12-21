package fun.haoyang666.www.socket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.MessageTypeEnum;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.impl.MatchImpl;
import fun.haoyang666.www.utils.MatchCacheUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.lang.reflect.Type;
import java.util.Map;

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
//        System.out.println("=============" + matchCacheUtil);
        System.out.println(matchImpl);
        log.info("ChatWebsocket open 连接建立完成 userId: {}", userId);
    }

   /* @OnError
    public void onError(Session session, Throwable error) {
        log.error("ChatWebsocket onError 发生了错误 userId: {}, errorMessage: {}", userId, error.getMessage());
        //移除客户端
        matchCacheUtil.removeClient(userId);
        //移除在线状态
        matchCacheUtil.removeUserOnlineStatus(userId);
        //移除对战室状态
        matchCacheUtil.removeUserFromRoom(userId);
        //移除游戏中的用户的对战信息
        matchCacheUtil.removeUserMatchInfo(userId);
        log.info("ChatWebsocket onError 连接断开完成 userId: {}", userId);
    }*/

    @OnClose
    public void onClose() {
        log.info("ChatWebsocket onClose 连接断开 userId: {}", userId);

        matchCacheUtil.removeClient(userId);
        matchCacheUtil.removeUserOnlineStatus(userId);
        matchCacheUtil.removeUserFromRoom(userId);
        matchCacheUtil.removeUserMatchInfo(userId);

        log.info("ChatWebsocket onClose 连接断开完成 userId: {}", userId);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("ChatWebsocket onMessage userId: {}, 来自客户端的消息 message: {}", userId, message);
        Gson gson = new Gson();
        Type gsonType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(message, gsonType);
        MessageTypeEnum type = gson.fromJson(map.get("type"), MessageTypeEnum.class);
        log.info("ChatWebsocket onMessage userId: {}, 来自客户端的消息类型 type: {}", userId, type);
        if (type == MessageTypeEnum.ADD_USER) {
//            addUser(jsonObject);
        } else if (type == MessageTypeEnum.MATCH_USER) {
//            matchUser(jsonObject);
//            System.out.println("===========");
//            System.out.println(matchImpl);
            matchImpl.match(userId);
        } else if (type == MessageTypeEnum.CANCEL_MATCH) {
//            cancelMatch(jsonObject);
        } else if (type == MessageTypeEnum.PLAY_GAME) {
//            toPlay(jsonObject);
        } else if (type == MessageTypeEnum.GAME_OVER) {
//            gameover(jsonObject);
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        log.info("ChatWebsocket onMessage userId: {} 消息接收结束", userId);
    }

}
