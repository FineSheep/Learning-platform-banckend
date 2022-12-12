package fun.haoyang666.www.service.impl;

import com.google.gson.Gson;
import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.common.ResultUtils;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.StatusEnum;
import fun.haoyang666.www.domain.Questions;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.MatchService;
import fun.haoyang666.www.service.QuestionsService;
import fun.haoyang666.www.socket.MatchSocket;
import fun.haoyang666.www.utils.MatchCacheUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yang
 * @createTime 2022/12/12 20:41
 * @description
 */
@Service
@Slf4j
public class MatchImpl implements MatchService {

    @Autowired
    private QuestionsService questionsService;
    @Autowired
    private MatchCacheUtil matchCacheUtil;
    private Lock lock = new ReentrantLock();

    private Condition matchCond = lock.newCondition();


    public <T> void sendMessage(Set<String> ids, T data) {
        ids.forEach(userId -> {
            try {
                MatchSocket client = matchCacheUtil.getClient(userId);
                Gson gson = new Gson();
                String json = gson.toJson(ResultUtils.success(data));
                client.getSession().getBasicRemote().sendText(json);
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        });


    }

    public void sendQues(Set<String> ids) {
        List<Questions> ques = questionsService.getQuesRandom(20);
        sendMessage(ids, ques);
    }
    @Override
    public void match(String userId) {
        log.info("ChatWebsocket matchUser 用户随机匹配对手开始 , userId: {}", userId);
        //保证原子性
        lock.lock();
        try {
            // 设置用户状态为匹配中
            matchCacheUtil.setUserInMatch(userId);
            matchCond.signal();
        } finally {
            lock.unlock();
        }
        new Thread(() -> {
            boolean flag = true;
            String receiver = null;
            while (flag) {
                // 获取除自己以外的其他待匹配用户
                // 当前用户不处于待匹配状态
                lock.lock();
                try {
                    //当前用户不在匹配状态
                    if (matchCacheUtil.getUserOnlineStatus(userId).compareTo(StatusEnum.IN_GAME) == 0
                            || matchCacheUtil.getUserOnlineStatus(userId).compareTo(StatusEnum.GAME_OVER) == 0) {
                        log.info("ChatWebsocket matchUser 当前用户 {} 已退出匹配", userId);
                        return;
                    }
                    //当前用户取消匹配
                    if (matchCacheUtil.getUserOnlineStatus(userId).compareTo(StatusEnum.IDLE) == 0) {
                        log.info("ChatWebsocket matchUser 当前用户 {} 已退出匹配", userId);
                        HashSet<String> idSet = new HashSet<>();
                        idSet.add(userId);
                        sendMessage(idSet, Constant.CANCEL);
                    }
                    receiver = matchCacheUtil.getUserInMatchRandom(userId);
                    if (receiver != null) {
                        //对手取消匹配
                        if (matchCacheUtil.getUserOnlineStatus(receiver).compareTo(StatusEnum.IN_MATCH) != 0) {
                            log.info("ChatWebsocket matchUser 当前用户 {}, 匹配对手 {} 已退出匹配状态", userId, receiver);
                            HashSet<String> idSet = new HashSet<>();
                            idSet.add(userId);
                            sendMessage(idSet, Constant.CANCEL);
                        } else {
                            //加入对战房间
                            matchCacheUtil.setUserInGame(userId);
                            matchCacheUtil.setUserInGame(receiver);
                            matchCacheUtil.setUserInRoom(userId, receiver);
                            flag = false;
                            log.info("匹配成功{}---->{}", userId, receiver);
                            HashSet<String> ids = new HashSet<>();
                            ids.add(userId);
                            ids.add(receiver);
                            sendQues(ids);
                        }
                    } else {
                        try {
                            log.info("ChatWebsocket matchUser 当前用户 {} 无对手可匹配", userId);
                            matchCond.await();
                        } catch (InterruptedException e) {
                            log.error("ChatWebsocket matchUser 匹配线程 {} 发生异常: {}",
                                    Thread.currentThread().getName(), e.getMessage());
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }

        }).start();
    }
}
