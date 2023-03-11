package fun.haoyang666.www.domain.vo;

import fun.haoyang666.www.domain.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/1/15 14:35
 * @description
 */
@Data
public class PostVO {
    private Long id;
    private String description;
    private String content;
    private String photo;
    private Integer reviewStatus;
    private String reviewMessage;
    private Integer collectNum;
    private Integer commentNum;
    private Integer thumbNum;
    private String tags;
    private LocalDateTime createTime;
    private String title;
    private User user;
    private List<String> tagsName;
    private Boolean collected;
    private Boolean thumbed;


}
