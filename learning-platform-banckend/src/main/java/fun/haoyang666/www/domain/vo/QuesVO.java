package fun.haoyang666.www.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author yang
 * @createTime 2023/1/17 11:00
 * @description
 */
@Data
public class QuesVO {

    private Long id;

    private String content;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;
    /**
     * 题型  0-单选 1-多选
     */
    private int type;
}
