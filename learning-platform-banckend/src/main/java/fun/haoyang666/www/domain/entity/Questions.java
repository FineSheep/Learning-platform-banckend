package fun.haoyang666.www.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 题目
 * @TableName questions
 */
@TableName(value ="questions")
@Data
public class Questions implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 题目内容
     */
    @ExcelProperty("题目内容")
    private String content;

    /**
     * 选项A
     */
    @ExcelProperty("选项A")
    private String optionA;

    /**
     * 选项B
     */
    @ExcelProperty("选项B")
    private String optionB;

    /**
     * 选项C
     */
    @ExcelProperty("选项C")
    private String optionC;

    /**
     * 选项D
     */
    @ExcelProperty("选项D")
    private String optionD;

    /**
     * 正确选项
     */
    @ExcelProperty("正确选项")
    private String correct;

    /**
     * 解析
     */
    @ExcelProperty("解析")
    private String analyzation;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
    /**
     * 题型  0-单选 1-多选 2-判断
     */
    private int type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}