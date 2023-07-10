package fun.haoyang666.www.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yang
 * @createTime 2023/3/12 19:48
 * @description
 */
@Data
public class InfoParamDto {
    private Long id;
    private String title;
    private Integer type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String source;
}
