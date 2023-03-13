package fun.haoyang666.www.admin.dto;

import lombok.Data;

/**
 * @author yang
 * @createTime 2023/3/5 12:40
 * @description
 */
@Data
public class UserParamReq {
    private Long userId;
    private String name;
    private String email;
    private Integer gender;
    private String phone;
    private Integer userStatus;
}
