package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    /**
     * userId
     */
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
     * 确认密码
     */
    private String confirmPassword;

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
    private Long version;

}
