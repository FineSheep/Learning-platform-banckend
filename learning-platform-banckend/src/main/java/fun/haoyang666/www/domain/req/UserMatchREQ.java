package fun.haoyang666.www.domain.req;

import lombok.Data;

/**
 * @author yang
 * @createTime 2023/1/31 14:02
 * @description
 */
@Data
public class UserMatchREQ {
    private Long userId;
    private int sum;
    private Long time;
}
