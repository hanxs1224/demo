package com.example.controller;

import com.example.common.ajax.AjaxResult;
import com.example.common.ajax.CallResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @RequestMapping("/")
    public AjaxResult<String> index() {
        AjaxResult<String> result=new AjaxResult<>(HttpStatus.OK.value(), CallResult.SUCCESS.getCode());
        result.setMsg("Greetings from Spring Boot!");
        return result;
    }
    
}
