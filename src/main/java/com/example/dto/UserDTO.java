package com.example.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    @ApiModelProperty(value = "userId")
    private Long userId;

    @NotNull(message = "用户名不能为空", groups = {UserValidateGroups1.class})
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotNull(message = "密码不能为空", groups = {UserValidateGroups1.class})
    @ApiModelProperty(value = "密码")
    private String password;

    @NotNull(message = "确认密码不能为空", groups = {UserValidateGroups1.class})
    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;

    @ApiModelProperty(value = "令牌")
    private String token;

    @ApiModelProperty(value = "令牌有效截止时间")
    private Date tokenTime;

    @ApiModelProperty(value = "激活状态（0：未激活,1：已激活）")
    private Boolean status;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank(message = "手机号不能为空", groups = {UserValidateGroups2.class})
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "用户角色英文名（以逗号分隔）")
    private String roles;

    private String creator;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

    private Long version;

    @NotNull(message = "注册类型不能为空", groups = {UserValidateGroups1.class})
    @ApiModelProperty(value = "注册类型（0：邮箱，1：手机）")
    private Long registerType;

}
