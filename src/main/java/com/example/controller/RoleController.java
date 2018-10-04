package com.example.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户角色 前端控制器
 * </p>
 *
 * @author hxs
 * @since 2018-09-16
 */
@RestController
@RequestMapping("/role")
@Api(value = "用户角色模块",tags = "角色管理")
public class RoleController {

}
