package fun.haoyang666.www.domain.dto;

import lombok.Data;

/**
 * @author yang
 * @createTime 2023/1/25 13:37
 * @description
 */
@Data
public class GradeDto {
    private Long quesId;
    private String correctAnswer;
    private String userAnswer;
    private boolean correct;
}
