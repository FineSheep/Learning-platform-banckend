package fun.haoyang666.www.domain.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yang
 * @createTime 2022/12/24 12:45
 * @description
 */
@Data
public class UserLoginByPassWordREQ implements Serializable {
    private String email;
    private String password;
}
