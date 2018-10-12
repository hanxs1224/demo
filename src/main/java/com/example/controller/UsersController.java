package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.common.ajax.AjaxResult;
import com.example.common.ajax.CallResult;
import com.example.common.utils.UserUtils;
import com.example.config.EnvConfig;
import com.example.dto.UserDTO;
import com.example.dto.UserValidateGroups1;
import com.example.dto.UserValidateGroups2;
import com.example.entity.Users;
import com.example.service.UsersService;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

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
@Api(value = "用户模块", tags = {"用户管理"})
public class UsersController {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private String emailRegexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    private String phoneNumberRegexp = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$";

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private EnvConfig envConfig;

    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @PostMapping("/userRegister")
    @ApiOperation(value = "userRegister", notes = "用户注册")
    public AjaxResult<Integer> userRegister(@RequestBody @Validated(UserValidateGroups1.class) UserDTO userDTO) {
        return Objects.equals(userDTO.getRegisterType(), 0L) ? registerByEmail(userDTO) : registerByMobile(userDTO);
    }

    private AjaxResult<Integer> registerByEmail(UserDTO userDTO) {
        AjaxResult<Integer> result = new AjaxResult<>(HttpStatus.OK.value(), CallResult.SUCCESS.getCode());
        if (!Pattern.matches(emailRegexp, userDTO.getUsername())) {
            result.setRst(CallResult.FAILURE.getCode());
            result.setMsg("邮箱格式错误");
            return result;
        }
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            result.setRst(CallResult.FAILURE.getCode());
            result.setMsg("密码和确认密码不一致");
            return result;
        }
        Users users = new Users();
        users.setUsername(userDTO.getUsername());
        users.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        users.setRoles("ROLE_USER");
        users.setRegisterType(userDTO.getRegisterType());
        usersService.addUser(users);

        sendEmail(userDTO.getUsername());

        return result;
    }

    private AjaxResult<Integer> registerByMobile(UserDTO userDTO) {
        AjaxResult<Integer> result = new AjaxResult<>(HttpStatus.OK.value(), CallResult.SUCCESS.getCode());
        if (!Pattern.matches(phoneNumberRegexp, userDTO.getUsername())) {
            result.setRst(CallResult.FAILURE.getCode());
            result.setMsg("手机号格式错误");
            return result;
        }
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            result.setRst(CallResult.FAILURE.getCode());
            result.setMsg("密码和确认密码不一致");
            return result;
        }
        Users users = new Users();
        users.setUsername(userDTO.getUsername());
        users.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        users.setRoles("ROLE_USER");
        users.setRegisterType(userDTO.getRegisterType());
        users.setStatus(true);
        usersService.addUser(users);
        return result;
    }

    private void sendEmail(String toEmail) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("name", toEmail);
            model.put("link", envConfig.getActivateUrl() + toEmail);

            Template template = freeMarkerConfig.getConfiguration().getTemplate("activate.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(envConfig.getMailSenderFrom());
            helper.setTo(toEmail);
            helper.setSubject("激活邮件");
            helper.setText(html, true);
            mailSender.send(message);
            log.info("邮件已发送");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @GetMapping("/findUserByToken")
    @ApiOperation(value = "findUserByToken", notes = "通过token查询用户信息")
    public AjaxResult<UserDTO> findUserByToken() {
        AjaxResult<UserDTO> result = new AjaxResult<>(HttpStatus.OK.value(), CallResult.SUCCESS.getCode());
        UserDTO dto = new UserDTO();
        Long userId = userUtils.getUserIdFromHeader(request);
        Users user = usersService.getById(userId);
        BeanUtils.copyProperties(user, dto);
        result.setData(dto);
        return result;
    }

    @GetMapping("activate/{email}")
    @ApiOperation(value = "activate/{email}", notes = "用户激活")
    public AjaxResult<Void> activate(@PathVariable(name = "email") String email) {
        AjaxResult<Void> result = new AjaxResult<>(HttpStatus.OK.value(), CallResult.SUCCESS.getCode());
        Users user = new Users();
        user.setStatus(true);
        user.setUpdatedTime(Date.from(Instant.now()));
        UpdateWrapper<Users> wrapper = new UpdateWrapper<>(new Users());
        wrapper.eq("username", email);
        usersService.update(user, wrapper);
        return result;
    }

    @PostMapping("updateUserMobile")
    @ApiOperation(value = "updateUserMobile", notes = "更新用户手机号")
    public AjaxResult<Void> updateUserMobile(@RequestBody @Validated(value = {UserValidateGroups2.class}) UserDTO userDTO) {
        AjaxResult<Void> result = new AjaxResult<>(HttpStatus.OK.value(), CallResult.SUCCESS.getCode());
        Users users = new Users();
        BeanUtils.copyProperties(userDTO, users);
        users.setUserId(userUtils.getUserIdFromHeader(request));
        users.setUpdatedTime(Date.from(Instant.now()));
        usersService.updateById(users);
        return result;
    }

}
