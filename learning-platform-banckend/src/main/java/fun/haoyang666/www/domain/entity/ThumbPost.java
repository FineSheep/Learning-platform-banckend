package fun.haoyang666.www.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author yang
 * @createTime 2023/1/16 11:44
 * @description
 */
@Data
@TableName("thumb_post")
public class ThumbPost {
    private long id;
    private long postId;
    private long userId;
}
