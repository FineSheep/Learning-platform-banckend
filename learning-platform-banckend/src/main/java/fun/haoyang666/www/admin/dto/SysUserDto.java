package fun.haoyang666.www.admin.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yang
 * @createTime 2023/3/5 11:55
 * @description
 */
@Data
public class SysUserDto implements Serializable {

    private Long id;

    private String username;

    private String email;

    private String avatarUrl;

    private Integer gender;

    private String phone;

    private Integer userStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer userRole;

    private String profile;

    private Integer questionNum;

    private Integer correctNum;

    private LocalDateTime birthday;
}
