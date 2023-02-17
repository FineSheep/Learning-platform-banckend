package fun.haoyang666.www.common.enums;

public enum ReadMenum {
    /**
     * 已读
     */
    READ(0L, "已读"),
    /**
     * 未读
     */
    UN_READ(1L, "未读");

    private Long code;
    private String message;

    ReadMenum(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    public Long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
