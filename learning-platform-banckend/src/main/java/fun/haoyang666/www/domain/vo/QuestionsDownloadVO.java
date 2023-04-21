package fun.haoyang666.www.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 题目
 *
 * @TableName questions
 */
@Data
public class QuestionsDownloadVO implements Serializable {


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

    private static final long serialVersionUID = 1L;
}