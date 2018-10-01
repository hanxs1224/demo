package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户角色 Mapper 接口
 * </p>
 *
 * @author hxs
 * @since 2018-09-16
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
