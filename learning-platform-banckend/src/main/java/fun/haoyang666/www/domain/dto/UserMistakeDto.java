package fun.haoyang666.www.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yang
 * @createTime 2023/1/5 15:07
 * @description
 */
@Data
public class UserMistakeDto {
    private long userId;
    private List<Long> records;
}
