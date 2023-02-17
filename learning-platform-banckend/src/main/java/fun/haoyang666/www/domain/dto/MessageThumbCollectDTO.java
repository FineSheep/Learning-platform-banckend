package fun.haoyang666.www.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yang
 * @createTime 2023/2/16 14:05
 * @description
 */
@Data
public class MessageThumbCollectDTO  {
    private Long messageId;
    private Long sendId;
    private String sendName;
    private String sendAvatar;
    private String title;
    private String content;
    private String postName;
    private Long isRead;
    private LocalDateTime time;
}
