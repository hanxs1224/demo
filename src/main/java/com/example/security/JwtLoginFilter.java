package com.example.security;

import com.alibaba.fastjson.JSON;
import com.example.common.ajax.AjaxResult;
import com.example.common.ajax.CallResult;
import com.example.dto.UserLoginDTO;
import com.example.entity.Users;
import com.example.service.JwtTokenService;
import com.example.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;
    private JwtTokenService jwtTokenService;
    private UsersService usersService;
    private ObjectMapper objectMapper;

    public JwtLoginFilter(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, UsersService usersService, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.usersService = usersService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication authentication;
        UsernamePasswordAuthenticationToken authenticationToken;
        UserLoginDTO dto = objectMapper.readValue(request.getReader(), UserLoginDTO.class);
        String username = dto.getUsername();
        String password = dto.getPassword();
        if (StringUtils.isBlank(username)) {
            authentication = new UsernamePasswordAuthenticationToken(null, "用户名不能为空");
            return authentication;
        }
        if (StringUtils.isBlank(password)) {
            authentication = new UsernamePasswordAuthenticationToken(null, "密码不能为空");
            return authentication;
        }
        Users users = usersService.findByUsernane(username);
        if (users == null) {
            authentication = new UsernamePasswordAuthenticationToken(null, "用户不存在");
            return authentication;
        }
        authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            authentication = new UsernamePasswordAuthenticationToken(null, "密码错误");
        }
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AjaxResult<Map> result = new AjaxResult<>(HttpStatus.OK.value(), CallResult.SUCCESS.getCode());
        if (authResult.getPrincipal() == null) {
            result.setRst(CallResult.FAILURE.getCode());
            result.setMsg(authResult.getCredentials().toString());
            result.setError("Unauthorized");
            result.setPath("/com/example/login");
        } else {
            String username = authResult.getName();
            Users users = usersService.findByUsernane(username);
            Users u = new Users();
            u.setUserId(users.getUserId());
            String token = jwtTokenService.genToken(users);
            String authorization = "Bearer " + token;
            u.setToken(authorization);
            u.setTokenTime(Date.from(Instant.now()));
            usersService.updateById(u);
            Map<String, String> data = new HashMap<>();
            data.put("Authorization", authorization);
            result.setData(data);
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(JSON.toJSONString(result).getBytes());
        response.flushBuffer();
    }

}
