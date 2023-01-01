package fun.haoyang666.www.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}