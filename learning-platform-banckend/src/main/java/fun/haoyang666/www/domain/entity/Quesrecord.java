package fun.haoyang666.www.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 题目记录关联
 * @TableName quesrecord
 */
@TableName(value ="quesrecord")
@Data
public class Quesrecord implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 记录id
     */
    private Long recordId;

    /**
     * 问题id
     */
    private Long questionId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户答案
     */
    private String userAnswer;

    /**
     * 正确与否 0-错误  1-正确
     */
    private Integer isCorrect;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}