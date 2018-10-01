package com.example.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JwtSettings {

    @Value("${jwt-security.token-signing-key}")
    private String tokenSigningKey;

    @Value("${jwt-security.timeout}")
    private Long timeout;

}
