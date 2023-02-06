package fun.haoyang666.www.domain.req;

import fun.haoyang666.www.common.enums.MessageTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author yang
 * @createTime 2023/1/31 15:52
 * @description
 */
@Data
public class PKREQ implements Serializable {

    private static final long serialVersionUID = -1729095441702160755L;
    private MessageTypeEnum type;
    private Map<Long, String> answer;
    private List<Long> quesIds;
    private String userId;
    private Long time;
    private String opponent;
    private Boolean result;

}
