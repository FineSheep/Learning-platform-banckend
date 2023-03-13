package fun.haoyang666.www.service.impl;

import cn.hutool.core.util.IdUtil;
import com.google.gson.Gson;
import fun.haoyang666.www.common.Constant;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.common.enums.StatusEnum;
import fun.haoyang666.www.domain.req.PKREQ;
import fun.haoyang666.www.domain.vo.*;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.QuestionsService;
import fun.haoyang666.www.socket.MatchSocket;
import fun.haoyang666.www.utils.MatchCacheUtil;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import fun.haoyang666.www.utils.ThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
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
public class MatchImpl {
    @Resource
    private MatchCacheUtil matchCacheUtil;
    @Resource
    private QuestionsService questionsService;
    private Lock lock = new ReentrantLock();
    private Condition matchCond = lock.newCondition();

    /**
     * 向成员发送题目
     *
     * @param id   接收者id
     * @param data 发送的信息
     * @param <T>
     */
    public <T> void sendMessage(T data, String id) {
        try {
            MatchSocket client = matchCacheUtil.getClient(id);
            Gson gson = new Gson();
            String json = gson.toJson(ResultUtils.success(data));
            client.getSession().getBasicRemote().sendText(json);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }


    /**
     * 匹配
     */
    public void match(String userId) {
        log.info("matchUser 用户随机匹配对手开始 , userId: {}", userId);
        //保证原子性
        lock.lock();
        try {
            // 设置用户状态为匹配中
            matchCacheUtil.setUserInMatch(userId);
            matchCond.signal();
        } finally {
            lock.unlock();
        }
        ExecutorService threadPool = ThreadPool.instance();
        threadPool.execute(() -> {
            boolean flag = true;
            String opponent = null;
            while (flag) {
                // 获取除自己以外的其他待匹配用户
                // 当前用户不处于待匹配状态
                lock.lock();
                try {
                    log.info("============用户：{}开始循环匹配============", userId);
                    //当前用户不在匹配状态
                    if (matchCacheUtil.getUserOnlineStatus(userId).compareTo(StatusEnum.IN_GAME) == 0
                            || matchCacheUtil.getUserOnlineStatus(userId).compareTo(StatusEnum.GAME_OVER) == 0) {
                        log.info("matchUser 当前用户 {} 已退出匹配", userId);
                        return;
                    }
                    //当前用户取消匹配
                    if (matchCacheUtil.getUserOnlineStatus(userId).compareTo(StatusEnum.IDLE) == 0) {
                        log.info("matchUser 当前用户 {} 已退出匹配", userId);
                        sendMessage(Constant.CANCEL, userId);
                    }
                    opponent = matchCacheUtil.getUserInMatchRandom(userId);
                    if (opponent != null) {
                        //对手取消匹配
                        if (matchCacheUtil.getUserOnlineStatus(opponent).compareTo(StatusEnum.IN_MATCH) != 0) {
                            log.info("matchUser 当前用户 {}, 匹配对手 {} 已退出匹配状态", userId, opponent);
                            sendMessage(Constant.CANCEL, userId);
                        } else {
                            //设置游戏状态
                            matchCacheUtil.setUserInGame(userId);
                            matchCacheUtil.setUserInGame(opponent);
                            matchCacheUtil.setUserInRoom(userId, opponent);
                            flag = false;
                            log.info("匹配成功{}---->{}", userId, opponent);
                            //双方发送匹配信息
                            sendQues(userId, opponent);
                          /*  sendMessage(opponent, userId);
                            sendMessage(userId, opponent);*/
                        }
                    } else {
                        try {
                            log.info("matchUser 当前用户 {} 无对手可匹配", userId);
                            matchCond.await();
                        } catch (InterruptedException e) {
                            log.error("matchUser 匹配线程 {} 发生异常: {}",
                                    Thread.currentThread().getName(), e.getMessage());
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }

        });
    }

    public void sendQues(String userId, String opponent) {
        Map<Integer, List<QuesVO>> questions = questionsService.getQuesRandom();
        sendMessage(new PKVO(questions, opponent), userId);
        sendMessage(new PKVO(questions, userId), opponent);
    }

    public void cancel(String userId) {
        log.info("取消匹配");
        matchCacheUtil.setUserIDLE(userId);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public synchronized void gameOver(PKREQ pkreq) {
        boolean userRoom = matchCacheUtil.getUserRoom(pkreq.getOpponent());
        String userId = pkreq.getUserId();
        matchCacheUtil.removeRoom(userId);
        CorrectVO vo = questionsService.beforeJudge(pkreq.getQuesIds(), pkreq.getAnswer(), pkreq.getTime());
        vo.setUserId(userId);
        matchCacheUtil.setUserMatchInfo(userId, vo);
        if (userRoom) {
            sendMessage("对手还在游戏中，请耐心等待。", userId);
        } else {
            //判断输赢并写入数据库
            String opponent = pkreq.getOpponent();
            CorrectVO userInfo = getVo(userId);
            log.info("vo1:{}", userInfo);
            CorrectVO opponentInfo = getVo(opponent);
            log.info("vo2:{}", opponentInfo);
            String victoryId = judgeVictory(userInfo, opponentInfo);
            log.info("victoryId:{}", victoryId);

            //写入胜利者
            PKResultVO victory;
            PKResultVO failure;
            String pkId = IdUtil.nanoId();
            if (victoryId.equals(userId)) {
                victory = convertPKResultVO(userInfo, true, opponent, pkId);
                failure = convertPKResultVO(opponentInfo, false, userId, pkId);
            } else {
                victory = convertPKResultVO(opponentInfo, true, userId, pkId);
                failure = convertPKResultVO(userInfo, false, opponent, pkId);
            }
            //写入数据库
            questionsService.PKGrade(victory);
            questionsService.PKGrade(failure);
            //移除房间以及对战信息
            sendMessage("赶快查看对战记录吧。", userId);


        }
    }

    public PKResultVO convertPKResultVO(CorrectVO vo, Boolean isVictory, String opponent, String pkId) {
        PKResultVO pkResultVO = new PKResultVO();
        pkResultVO.setResult(isVictory);
        pkResultVO.setOpponent(opponent);
        pkResultVO.setCorrectList(vo.getCorrect());
        pkResultVO.setFailureList(vo.getFailure());
        pkResultVO.setUserId(vo.getUserId());
        pkResultVO.setTime(vo.getTime());
        pkResultVO.setPkId(pkId);
        return pkResultVO;
    }

    /**
     * 获取redis中信息
     */
    public CorrectVO getVo(String id) {
        CorrectVO correctVO = (CorrectVO) matchCacheUtil.getUserMatchInfo(id);
        return correctVO;
    }

    /**
     * 返回获胜者id
     *
     * @param userInfo
     * @param opponentInfo
     * @return
     */
    public String judgeVictory(CorrectVO userInfo, CorrectVO opponentInfo) {
        if (userInfo.getCorrect().size() > opponentInfo.getCorrect().size()) {
            return userInfo.getUserId();
        }
        if (userInfo.getCorrect().size() == opponentInfo.getCorrect().size()) {
            if (userInfo.getTime() > opponentInfo.getTime()) {
                return opponentInfo.getUserId();
            }
        }
        return opponentInfo.getUserId();
    }
}
