package fun.haoyang666.www.admin.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yang
 * @createTime 2023/3/7 12:13
 * @description
 */
@Data
public class SysPostDto {
    private Long id;
    private String title;
    private String description;
    private String username;
    private Long userId;
    private String tag;
    private Long state;
}
