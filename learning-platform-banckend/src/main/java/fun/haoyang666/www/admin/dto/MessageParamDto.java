package fun.haoyang666.www.admin.dto;

import fun.haoyang666.www.domain.dto.UserDTO;
import lombok.Data;

/**
 * @author yang
 * @createTime 2023/3/11 20:49
 * @description
 */
@Data
public class MessageParamDto {
    private Long id;
    private Integer type;
    private Integer read;
    private String content;
    private String title;
    private Long userId;
    private Long deal;
}
