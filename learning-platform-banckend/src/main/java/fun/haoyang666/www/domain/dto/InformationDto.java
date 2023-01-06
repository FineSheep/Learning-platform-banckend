package fun.haoyang666.www.domain.dto;

import fun.haoyang666.www.domain.entity.Information;
import lombok.Data;

import java.util.List;

/**
 * @author yang
 * @createTime 2022/12/31 17:06
 * @description
 */
@Data
public class InformationDto {
    List<Information> records;
    boolean hasNext;
}
