package fun.haoyang666.www.common.enums;

/**
 * success 枚举
 *
 * @author yang
 */

public enum SuccessCode {
    /**
     * 成功
     */
    SUCCESS(200, "成功");

    SuccessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;
}
