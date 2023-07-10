package fun.haoyang666.www.admin.dto;

import lombok.Data;

/**
 * @author yang
 * @createTime 2023/3/11 15:11
 * @description
 */
@Data
public class MessageSendDto {
    private String type;
    private Long userId;
    private String content;
}
