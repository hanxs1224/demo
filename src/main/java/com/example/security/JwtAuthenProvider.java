package com.example.security;

import com.example.service.UsersService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

public class JwtAuthenProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder encoder;
    private final UsersService usersService;

    public JwtAuthenProvider(BCryptPasswordEncoder encoder, UsersService usersService) {
        this.encoder = encoder;
        this.usersService = usersService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        if (username == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDetails userDetails = usersService.loadUserByUsername(username);
        if (!encoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Authentication Failed. Password invalid.");
        }
        return new UsernamePasswordAuthenticationToken(username, password, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
