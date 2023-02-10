package fun.haoyang666.www.domain.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/1/14 21:05
 * @description
 */
@Data
public class SavePostREQ implements Serializable {
    private long userId;
    private List<Long> tags;
    private String content;
    private String title;
    private String description;
    private String photo;
}
