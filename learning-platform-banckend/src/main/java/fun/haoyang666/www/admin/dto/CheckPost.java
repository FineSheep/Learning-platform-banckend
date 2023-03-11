package fun.haoyang666.www.admin.dto;

import lombok.Data;

/**
 * @author yang
 * @createTime 2023/3/8 16:44
 * @description
 */
@Data
public class CheckPost {
    private Long postId;
    private Integer reviewStatus;
    private String reviewMessage;
}
