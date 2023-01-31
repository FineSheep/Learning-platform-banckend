package fun.haoyang666.www.domain.req;

import lombok.Data;

/**
 * @author yang
 * @createTime 2023/1/16 22:59
 * @description
 */
@Data
public class GetQuesREQ {
    private long userId;
    private long sum;
    private String source;
    private String difficulty;
}
