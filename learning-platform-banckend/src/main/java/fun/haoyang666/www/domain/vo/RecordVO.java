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
public class RecordVO implements Serializable {
    private static final long serialVersionUID = -5489787637306748545L;
    private Long id;
    private Long answerTime;
    private Long userId;
    private Long opponent;
    private Integer result;
    private Long sum;
    private Long currectSum;

    public void setAnswerTime(LocalDateTime startTime, LocalDateTime endTime) {
        this.answerTime = Duration.between(startTime, endTime).toMillis() / 1000;
    }

}
