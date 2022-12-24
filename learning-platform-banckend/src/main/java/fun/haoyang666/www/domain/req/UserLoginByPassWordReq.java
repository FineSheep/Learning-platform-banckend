package fun.haoyang666.www.domain.req;

import lombok.Data;

/**
 * @author yang
 * @createTime 2022/12/24 12:45
 * @description
 */
@Data
public class UserLoginByPassWordReq {
    private String email;
    private String password;
}
