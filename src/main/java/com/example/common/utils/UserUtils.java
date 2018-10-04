package com.example.common.utils;

import com.example.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserUtils {

    private JwtTokenService jwtTokenService;

    @Autowired
    public UserUtils(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    public Long getUserIdFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String token = authorization.substring("Bearer ".length());
        Claims claims = jwtTokenService.parseToken(token);
        return Long.parseLong(claims.get("userId").toString());
    }

}
