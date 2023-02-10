package fun.haoyang666.www.domain.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yang
 * @createTime 2023/1/31 14:02
 * @description
 */
@Data
public class UserMatchREQ  implements Serializable {
    private Long userId;
    private int sum;
    private Long time;
}
