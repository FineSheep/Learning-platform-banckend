package fun.haoyang666.www.common.enums;

import fun.haoyang666.www.exception.BusinessException;

/**
 * @author yang
 * @description 题目来源枚举
 */
public enum QuesEnum {
    /**
     * 错题
     */
    MISTAKE_QUES("mistake"),
    /**
     * 新题
     */
    NEW_QUES("new"),
    /**
     * 混合
     */
    MIS_QUES("mix"),
    /**
     * 随机
     */
    RANDOM_QUES("random");
    private String message;

    QuesEnum(String message) {
        this.message = message;
    }

    public static QuesEnum getQuesEnum(String message) {
        switch (message) {
            case "mistake":
                return MISTAKE_QUES;
            case "new":
                return NEW_QUES;
            case "mix":
                return MIS_QUES;
            case "random":
                return RANDOM_QUES;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择正确的类型");
        }
    }
}
