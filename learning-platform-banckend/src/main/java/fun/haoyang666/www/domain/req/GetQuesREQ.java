package fun.haoyang666.www.domain.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yang
 * @createTime 2023/1/16 22:59
 * @description
 */
@Data
public class GetQuesREQ implements Serializable {
    private long userId;
    private long sum;
    private String source;
    private String difficulty;
}
