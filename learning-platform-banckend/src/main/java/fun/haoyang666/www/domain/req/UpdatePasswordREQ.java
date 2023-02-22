package fun.haoyang666.www.domain.req;

import lombok.Data;

/**
 * @author yang
 * @createTime 2023/2/20 16:36
 * @description
 */
@Data
public class UpdatePasswordREQ {
//    private Long userId;
    private String onePass;
    private String twoPass;
    private String code;
}
