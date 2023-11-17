package com.nisum.challenge.usersms.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(value = "com.nisum.challenge.usersms")
public class AppConfig {
    private String validateEmailExp;
    private String validatePasswordExp;
}
