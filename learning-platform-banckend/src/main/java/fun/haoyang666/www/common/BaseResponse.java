package fun.haoyang666.www.common;

import fun.haoyang666.www.common.enums.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @author yang
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -6103646700230028845L;
    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;

    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");

    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
