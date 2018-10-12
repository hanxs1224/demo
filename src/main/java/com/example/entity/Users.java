package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author hxs
 * @since 2018-09-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 令牌
     */
    private String token;

    /**
     * 令牌有效截止时间
     */
    private Date tokenTime;

    /**
     * 激活状态（0：未激活,1：已激活）
     */
    private Boolean status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户角色英文名（以逗号分隔）
     */
    private String roles;

    /**
     * 用户角色列表
     */
    @TableField(exist = false)
    private List<UserRole> roleList;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 版本号
     */
    @Version
    private Long version;

    /**
     * 注册类型（0：邮箱，1：手机）
     */
    private Long registerType;

}
