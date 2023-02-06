package fun.haoyang666.www.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yang
 * @createTime 2023/2/1 16:23
 * @description
 */
@Data
public class PKResultVO {
    private String userId;
    private String opponent;
    private List<GradeVO> correctList;
    private List<GradeVO> failureList;
    private Long time;
    private Boolean result;
}
