package com.example.security;

import com.alibaba.fastjson.JSON;
import com.example.common.ajax.AjaxResult;
import com.example.common.ajax.CallResult;
import com.example.config.JwtSettings;
import com.example.entity.Users;
import com.example.service.JwtTokenService;
import com.example.service.UsersService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtLogoutHandler implements LogoutHandler {

    private JwtTokenService jwtTokenService;
    private UsersService usersService;
    private JwtSettings settings;

    public JwtLogoutHandler(JwtTokenService jwtTokenService, UsersService usersService, JwtSettings settings) {
        this.jwtTokenService = jwtTokenService;
        this.usersService = usersService;
        this.settings = settings;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorization = request.getHeader("Authorization");
        String bearer = "Bearer ";
        boolean tokenValid = false;
        if (authorization != null && authorization.startsWith(bearer)) {
            String token = authorization.substring(bearer.length());
            Claims claims = jwtTokenService.parseToken(token);
            String username = claims.getSubject();
            if (StringUtils.isNoneEmpty(username)) {
                Users users = usersService.findByUsernane(username);
                long tokenTime = users.getTokenTime().getTime();
                long currentTime = Date.from(Instant.now()).getTime();
                if (authorization.equals(users.getToken()) && currentTime < tokenTime + settings.getTimeout()) {
                    SecurityContextHolder.clearContext();
                    Users u = new Users();
                    u.setUserId(users.getUserId());
                    u.setToken(" ");
                    usersService.updateById(u);
                    tokenValid = true;
                }
            }
        }
        AjaxResult<Map> result = new AjaxResult<>(HttpStatus.OK.value(), CallResult.SUCCESS.getCode());
        result.setRst(tokenValid ? CallResult.SUCCESS.getCode() : CallResult.FAILURE.getCode());
        result.setMsg(tokenValid ? "退出成功" : "无效的token");
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().write(JSON.toJSONString(result).getBytes());
            response.flushBuffer();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
