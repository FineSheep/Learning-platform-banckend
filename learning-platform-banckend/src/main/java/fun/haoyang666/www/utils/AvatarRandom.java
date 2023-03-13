package fun.haoyang666.www.utils;

import java.util.Random;

/**
 * @author yang
 * @createTime 2023/3/13 13:00
 * @description
 */

public class AvatarRandom {
    /**
     * 随机头像地址
     */
    private static final String[] AVATAR = new String[]{
            "http://cdn.haoyang666.fun/school/person/person-avatar/mmexport1673884054705.jpg",
            "http://cdn.haoyang666.fun/school/person/person-avatar/mmexport1673884055136.jpg",
            "http://cdn.haoyang666.fun/school/person/person-avatar/mmexport1674609511583.jpg",
            "http://cdn.haoyang666.fun/school/person/person-avatar/mmexport1674609511793.jpg",
            "http://cdn.haoyang666.fun/school/person/person-avatar/mmexport1674609512018.jpg",
            "http://cdn.haoyang666.fun/school/person/person-avatar/mmexport1674609512264.jpg",
            "http://cdn.haoyang666.fun/school/person/person-avatar/mmexport1674609512539.jpg"
    };

    public static String randomAvatar() {
        Random random = new Random();
        int index = random.nextInt(AVATAR.length);
        return AVATAR[index];
    }
}
