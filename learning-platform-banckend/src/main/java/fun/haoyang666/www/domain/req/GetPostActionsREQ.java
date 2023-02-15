package fun.haoyang666.www.domain.req;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author yang
 * @createTime 2023/2/15 16:32
 * @description
 */
@Data
public class GetPostActionsREQ extends PageREQ {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ArrayList tags;
    private Long userId;
    private Integer offset;
}
