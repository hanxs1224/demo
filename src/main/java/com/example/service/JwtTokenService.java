package com.example.service;

import com.example.entity.Users;
import io.jsonwebtoken.Claims;

public interface JwtTokenService {

    String genToken(Users users);

    Claims parseToken(String token);

}
