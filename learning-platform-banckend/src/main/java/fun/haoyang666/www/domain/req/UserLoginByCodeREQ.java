package fun.haoyang666.www.domain.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yang
 * @createTime 2022/12/24 17:18
 * @description
 */
@Data
public class UserLoginByCodeREQ implements Serializable {
    private String email;
    private String code;
}
