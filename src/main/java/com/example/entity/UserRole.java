package com.example.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户角色
 * </p>
 *
 * @author hxs
 * @since 2018-09-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements Serializable {

    @TableId(value = "user_role_id", type = IdType.AUTO)
    private Long userRoleId;

    /**
     * 角色中文名
     */
    private String roleNameCn;

    /**
     * 角色英文名
     */
    private String roleNameEn;

    /**
     * 状态（0：无效，1：有效）
     */
    private Boolean status;

    private String creator;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

    @Version
    private Long version;


}
