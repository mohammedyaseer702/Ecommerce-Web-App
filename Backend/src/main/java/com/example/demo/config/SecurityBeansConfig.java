package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityBeansConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new RuntimeException("UserDetailsService not implemented");
        };
    }
}
