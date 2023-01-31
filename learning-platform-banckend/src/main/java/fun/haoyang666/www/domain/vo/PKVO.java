package fun.haoyang666.www.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author yang
 * @createTime 2023/1/30 14:24
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PKVO {
    private Map<Integer, List<QuesVO>> ques;
    private String opponent;
}
