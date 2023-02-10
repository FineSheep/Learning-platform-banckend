package fun.haoyang666.www.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yang
 * @createTime 2023/2/10 14:58
 * @description
 */
@TableName("comment")
@Data
public class Comment implements Serializable {
    private String id;
    private String parentId;
    private String content;
    private Long postId;
    private LocalDateTime createTime;
    private String userId;
}
