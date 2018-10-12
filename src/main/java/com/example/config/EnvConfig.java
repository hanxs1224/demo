package com.example.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EnvConfig {

    @Value("#{'${spring.mail.username}'}")
    private String mailSenderFrom;

    @Value("#{'${baseUrl}'.concat('/com/example/users/activate/')}")
    private String activateUrl;

}
