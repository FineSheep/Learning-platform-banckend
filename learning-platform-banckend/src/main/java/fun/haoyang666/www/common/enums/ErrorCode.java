package fun.haoyang666.www.common.enums;

/**
 * 错误码
 *
 * @author yang
 */
public enum ErrorCode {
    PARAMS_ERROR(40000, "用户发生错误", ""),
    LOGIN_EXPIRE(40001, "登录过期", ""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    NO_AUTH(40003,"暂无操作权限","");

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
