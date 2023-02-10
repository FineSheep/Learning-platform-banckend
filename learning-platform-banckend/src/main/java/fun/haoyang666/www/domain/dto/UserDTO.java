package fun.haoyang666.www.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yang
 * @createTime 2022/12/24 12:43
 * @description
 */
@Data
public class UserDTO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;


    /**
     * 电话
     */
    private String phone;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 做题数
     */
    private Integer questionNum;

    /**
     * 正确数
     */
    private Integer correctNum;
}
