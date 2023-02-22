package fun.haoyang666.www.utils;

/**
 * @author yang
 * @createTime 2023/2/21 19:44
 * @description
 */

public class ThreadLocalUtils {

    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(Long userId) {
        THREAD_LOCAL.set(userId);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static Long get() {
        return THREAD_LOCAL.get();
    }
}
