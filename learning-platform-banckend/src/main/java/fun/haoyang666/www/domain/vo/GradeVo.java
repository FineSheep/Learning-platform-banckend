package fun.haoyang666.www.domain.vo;

import lombok.Data;

/**
 * @author yang
 * @createTime 2023/1/25 13:37
 * @description
 */
@Data
public class GradeVo {
    private Long quesId;
    private String correctAnswer;
    private String userAnswer;
    private boolean correct;
}
