package fun.haoyang666.www.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yang
 * @createTime 2023/2/16 10:01
 * @description
 */
@Data
@TableName("message_user")
public class MessageUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long messageId;
    private Long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long isRead;
    private LocalDateTime readTime;
    private Integer type;
    @TableLogic
    private Integer isDelete;
}
