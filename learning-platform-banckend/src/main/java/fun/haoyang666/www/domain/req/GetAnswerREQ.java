package fun.haoyang666.www.domain.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author yang
 * @createTime 2023/1/24 14:42
 * @description
 */
@Data
public class GetAnswerREQ implements Serializable {
    private Map<Long, String> answer;
    private List<Long> quesIds;
//    private Long userId;
    private Long time;
    private Long opponent;
    private Boolean result;
}
