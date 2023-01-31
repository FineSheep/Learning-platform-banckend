package fun.haoyang666.www.domain.req;

import lombok.Data;

/**
 * @author yang
 * @createTime 2022/12/17 22:00
 * @description
 */
@Data
public class UserRegisterREQ {
    private String email;
    private String password;
    private String checkPassword;
    private String UserCode;
}
