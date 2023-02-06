package fun.haoyang666.www.domain.dto;

import fun.haoyang666.www.common.enums.MessageTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yang
 * @createTime 2023/1/31 15:52
 * @description
 */
@Data
public class PKDTO implements Serializable {

    private static final long serialVersionUID = -1729095441702160755L;
    private MessageTypeEnum type;
    private String receiver;

}
