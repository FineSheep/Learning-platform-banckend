package fun.haoyang666.www.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yang
 * @createTime 2023/2/16 15:06
 * @description
 */
@Data
public class MessageResultDTO {
    private List<MessageThumbCollectDTO> data;
    private Long count;
}
