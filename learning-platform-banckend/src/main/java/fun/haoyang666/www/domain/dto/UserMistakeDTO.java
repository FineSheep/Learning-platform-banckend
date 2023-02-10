package fun.haoyang666.www.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/1/5 15:07
 * @description
 */
@Data
public class UserMistakeDTO  implements Serializable {
    private long userId;
    private List<Long> records;
}
