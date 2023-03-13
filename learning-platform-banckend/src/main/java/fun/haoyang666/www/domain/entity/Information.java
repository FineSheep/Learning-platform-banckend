package fun.haoyang666.www.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 
 * @TableName information
 */
@TableName(value ="information")
@Data
public class Information implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 发表时间
     */
    private LocalDate putTime;

    /**
     * 来源
     */
    private String source;

    /**
     * 内容
     */
    private String content;

    /**
     * 链接
     */
    private String link;

    /**
     * 图片链接
     */
    private String photo;

    private Integer type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}