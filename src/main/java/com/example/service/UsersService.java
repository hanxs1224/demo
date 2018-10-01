package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author hxs
 * @since 2018-09-16
 */
public interface UsersService extends IService<Users>, UserDetailsService {

    Integer addUser(Users users);

    Users findByUsernane(String username);

}
