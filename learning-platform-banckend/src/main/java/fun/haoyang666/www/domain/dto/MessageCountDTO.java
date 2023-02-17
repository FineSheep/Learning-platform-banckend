package fun.haoyang666.www.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yang
 * @createTime 2023/2/17 10:15
 * @description
 */
@Data
public class MessageCountDTO implements Serializable {
    private Integer thumbCollect;
    private Integer system;
    private Integer comment;
}
