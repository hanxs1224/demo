package com.example.service.impl;

import com.example.config.JwtSettings;
import com.example.entity.Users;
import com.example.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    @Autowired
    private JwtSettings settings;

    @Override
    public String genToken(Users users) {
        Claims claims= Jwts.claims().setSubject(users.getUsername());
        claims.put("userId",users.getUserId());
        claims.put("createdTime",Date.from(Instant.now()));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();
    }

    @Override
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(settings.getTokenSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

}
