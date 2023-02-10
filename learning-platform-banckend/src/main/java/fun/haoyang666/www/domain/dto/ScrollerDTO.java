package fun.haoyang666.www.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/1/8 11:37
 * @description
 */
@Data
public class ScrollerDTO<T>  implements Serializable {
    private List<T> records;
    private boolean hasNext;
}
