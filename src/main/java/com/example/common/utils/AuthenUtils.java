package com.example.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class AuthenUtils {

    public static List<SimpleGrantedAuthority> buildAuthen(String roles){
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();
        if (StringUtils.isNoneEmpty(roles)){
            String[] roleArr= StringUtils.splitByWholeSeparator(roles,",");
            for (String role:roleArr){
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        return authorities;
    }

}
