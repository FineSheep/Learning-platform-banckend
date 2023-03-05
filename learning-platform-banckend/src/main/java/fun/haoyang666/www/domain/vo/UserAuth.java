package fun.haoyang666.www.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yang
 * @createTime 2023/3/4 22:49
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth {
    private Long userId;
    private String auth;
}
