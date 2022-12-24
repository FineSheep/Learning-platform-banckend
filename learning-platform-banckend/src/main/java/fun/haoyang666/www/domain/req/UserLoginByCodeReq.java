package fun.haoyang666.www.domain.req;

import lombok.Data;

/**
 * @author yang
 * @createTime 2022/12/24 17:18
 * @description
 */
@Data
public class UserLoginByCodeReq {
    private String email;
    private String code;
}
