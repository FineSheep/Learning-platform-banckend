package fun.haoyang666.www.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/2/1 16:08
 * @description
 */
@Data
public class CorrectVO implements Serializable {
    private static final long serialVersionUID = -4548290257513769928L;
    private List<GradeVO> correct;
    private List<GradeVO> failure;
    private Long time;
    private String userId;
}
