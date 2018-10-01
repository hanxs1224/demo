package com.example.security;

import com.example.common.utils.AuthenUtils;
import com.example.config.JwtSettings;
import com.example.entity.Users;
import com.example.exception.TokenInvalidException;
import com.example.service.JwtTokenService;
import com.example.service.UsersService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

@Slf4j
public class JwtAuthenFilter extends OncePerRequestFilter {

    private JwtTokenService jwtTokenService;
    private UsersService usersService;
    private JwtSettings settings;

    public JwtAuthenFilter(JwtTokenService jwtTokenService, UsersService usersService, JwtSettings settings) {
        this.jwtTokenService = jwtTokenService;
        this.usersService = usersService;
        this.settings = settings;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String bearer = "Bearer ";
        SecurityContextHolder.getContext().setAuthentication(null);
        if (authorization != null && authorization.startsWith(bearer)) {
            String token = authorization.substring(bearer.length());
            Claims claims = jwtTokenService.parseToken(token);
            String username = claims.getSubject();
            if (StringUtils.isNoneEmpty(username)) {
                Users users = usersService.findByUsernane(username);
                if (users != null && StringUtils.isNoneEmpty(users.getToken())) {
                    long currentTime = Date.from(Instant.now()).getTime();
                    long tokenTime = users.getTokenTime().getTime();
                    if (authorization.equals(users.getToken()) && currentTime < tokenTime + settings.getTimeout()) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(users.getUsername(),
                                users.getPassword(), AuthenUtils.buildAuthen(users.getRoles()));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        Users u = new Users();
                        u.setUserId(users.getUserId());
                        u.setTokenTime(Date.from(Instant.now()));
                        usersService.updateById(u);
                    } else {
                        if (currentTime > tokenTime + settings.getTimeout()) {
                            throw new TokenInvalidException("Token已失效");
                        } else {
                            throw new TokenInvalidException("此账号已在其他地方登陆,导致Token已失效");
                        }
                    }
                } else {
                    throw new TokenInvalidException("获取用户信息失败");
                }
            } else {
                throw new TokenInvalidException("获取用户信息失败");
            }
        }
        filterChain.doFilter(request, response);
    }
}
