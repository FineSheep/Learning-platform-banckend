package fun.haoyang666.www.common.enums;

/**
 * @author yang
 * @createTime 2023/2/16 10:19
 * @description
 */

public enum SysMessageEnum {

    /**
     * 点赞、收藏
     */
    THUMB_COLLECT("0", "点赞、收藏"),
    /**
     * 评论
     */
    COMMENT("3", "评论"),
    /**
     * 系统
     */
    SYSTEM("1", "系统"),
    /**
     * 反馈
     */
    BACK("2", "反馈");
    private String code;
    private String message;

    SysMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
