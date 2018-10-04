package com.example.controller;


import com.example.common.ajax.AjaxResult;
import com.example.common.ajax.CallResult;
import com.example.dto.UserDTO;
import com.example.entity.Users;
import com.example.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author hxs
 * @since 2018-09-16
 */
@Slf4j
@RestController
@RequestMapping("/users")
@Api(value = "用户模块",tags = {"用户管理"})
public class UsersController {

    private HttpServletRequest request;
    private HttpServletResponse response;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsersService usersService;

    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @PostMapping("/userRegister")
    @ApiOperation(value = "userRegister",notes = "用户注册")
    public AjaxResult<Integer> userRegister(@RequestBody UserDTO userDTO) {
        log.info("{}", userDTO);
        AjaxResult<Integer> result=new AjaxResult<>(HttpStatus.OK.value(), CallResult.SUCCESS.getCode());
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            result.setRst(CallResult.FAILURE.getCode());
            result.setMsg("密码和确认密码不一致");
            return result;
        }
        Users users = new Users();
        users.setUsername(userDTO.getUsername());
        users.setEmail(userDTO.getUsername());
        users.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        users.setRoles("ROLE_USER");

        Integer i = usersService.addUser(users);
        return result;
    }

}
