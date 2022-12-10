package com.haoyang666.fun.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 帖子
 *
 * @TableName post
 */
@TableName(value = "post")
@Data
public class Post implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 描述
     */
    private String description;

    /**
     * 内容
     */
    private String content;

    /**
     * 照片地址
     */
    private String photo;

    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 收藏数
     */
    private Integer collectNum;

    /**
     * 评论数
     */
    private Integer commentNum;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 文章标题
     */
    private String title;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}