package fun.haoyang666.www.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author yang
 * @createTime 2023/1/16 11:44
 * @description
 */
@Data
@TableName("collect_post")
public class CollectPost {
    private long id;
    private long postId;
    private long userId;
}
