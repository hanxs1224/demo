package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.AuthenUtils;
import com.example.entity.Users;
import com.example.mapper.UsersMapper;
import com.example.service.UsersService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hxs
 * @since 2018-09-16
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService, UserDetailsService {

    @Resource
    private UsersMapper usersMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Users> wrapper = new QueryWrapper<>(new Users());
        wrapper.eq("username", username);
        Users users = usersMapper.selectOne(wrapper);
        return new User(users.getUsername(), users.getPassword(), AuthenUtils.buildAuthen(users.getRoles()));
    }

    @Override
    public Integer addUser(Users users) {
        return usersMapper.insert(users);
    }

    @Override
    public Users findByUsernane(String username) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>(new Users());
        wrapper.eq("username", username);
        return usersMapper.selectOne(wrapper);
    }


}
