package fun.haoyang666.www.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.domain.entity.Tag;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/1/15 14:35
 * @description
 */
@Data
public class PostVo {
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
    private LocalDate createTime;
    private String title;
    private User user;
    private List<Tag> tagList;


}
