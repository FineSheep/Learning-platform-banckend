package fun.haoyang666.www.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户做题记录
 *
 * @TableName records
 */
@TableName(value = "records")
@Data
public class Records implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 对手id
     */
    private Long opponent;

    /**
     * 游戏结果 0-输  1-赢
     */
    private Integer result;
    /**
     * 是否pk
     */
    private Integer PK;

    /**
     * 题目总数
     */
    private Long sum;

    /**
     * 正确数量
     */
    private Long currectSum;

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
    @TableLogic
    private Integer isDelete;

    private String pkId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}