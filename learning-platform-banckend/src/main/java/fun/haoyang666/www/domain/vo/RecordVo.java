package fun.haoyang666.www.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author yang
 * @createTime 2023/1/8 14:24
 * @description
 */
@Data
public class RecordVo implements Serializable {

    private static final long serialVersionUID = -5489787637306748545L;

    private Long id;

    private Long answerTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 对手id
     */
    private Long opponent;

    /**
     * 游戏结果 0-输  1-赢
     */
    private Integer result;

    /**
     * 题目总数
     */
    private Long sum;

    /**
     * 正确数量
     */
    private Long currectSum;

    public void setAnswerTime(LocalDateTime startTime, LocalDateTime endTime) {
        this.answerTime = Duration.between(startTime, endTime).toMillis() / 1000;
    }

}
