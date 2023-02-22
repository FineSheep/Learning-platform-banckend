package fun.haoyang666.www.domain.req;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yang
 * @createTime 2023/2/11 17:22
 * @description
 */
@Data
public class UserInfoREQ {
//    private Long userId;
    private Integer gender;
    private String phone;
    private String profile;
    private String username;
    private LocalDateTime birthday;
}
