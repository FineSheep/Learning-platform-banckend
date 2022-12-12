package fun.haoyang666.www.domain.dto;

import fun.haoyang666.www.common.enums.MessageTypeEnum;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.Set;

/**
 * @author yang
 * @createTime 2022/12/12 20:37
 * @description
 */
@Data
public class MessageDto<T> {

    /**
     * 消息类型
     */
    private MessageTypeEnum type;

    /**
     * 消息发送者
     */
    private String sender;

    /**
     * 消息接收者,以防群发
     */
    private Set<String> receivers;

    private T data;
}
