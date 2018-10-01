package com.example.config;


import com.example.security.JwtAuthenFilter;
import com.example.security.JwtAuthenProvider;
import com.example.security.JwtLoginFilter;
import com.example.security.JwtLogoutHandler;
import com.example.service.JwtTokenService;
import com.example.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {

    private UsersService usersService;
    private JwtTokenService jwtTokenService;
    private ObjectMapper objectMapper;
    private JwtSettings settings;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public SecurityWebConfig(UsersService usersService, JwtTokenService jwtTokenService, ObjectMapper objectMapper, JwtSettings settings, BCryptPasswordEncoder encoder) {
        this.usersService = usersService;
        this.jwtTokenService = jwtTokenService;
        this.objectMapper = objectMapper;
        this.settings = settings;
        this.encoder = encoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new JwtAuthenProvider(encoder, usersService));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager();
        http.csrf().disable();
        http.cors().disable();
//        http.authorizeRequests()
//                .antMatchers("/com/example/users/userRegister/**").permitAll()
//                .anyRequest()
//                .authenticated();
        http.addFilterAfter(new JwtLoginFilter(authenticationManager, jwtTokenService, usersService, objectMapper), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new JwtAuthenFilter(jwtTokenService, usersService, settings), JwtLoginFilter.class);
        http.logout().logoutUrl("/com/example/logout").addLogoutHandler(new JwtLogoutHandler(jwtTokenService, usersService, settings));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

    }

}
