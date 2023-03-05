package fun.haoyang666.www.utils;

import fun.haoyang666.www.domain.vo.UserAuth;

/**
 * @author yang
 * @createTime 2023/2/21 19:44
 * @description
 */

public class ThreadLocalUtils {

    private static final ThreadLocal<UserAuth> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(UserAuth userAuth) {
        THREAD_LOCAL.set(userAuth);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static UserAuth get() {
        return THREAD_LOCAL.get();
    }
}
