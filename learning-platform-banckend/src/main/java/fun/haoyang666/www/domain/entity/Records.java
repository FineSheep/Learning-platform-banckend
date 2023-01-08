package fun.haoyang666.www.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户做题记录
 * @TableName records
 */
@TableName(value ="records")
@Data
public class Records implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 答题时间
     */
    private Long answerTime;

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
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}