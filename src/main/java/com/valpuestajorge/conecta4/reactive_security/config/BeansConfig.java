package com.valpuestajorge.conecta4.reactive_security.config;

import com.valpuestajorge.conecta4.security.encoder.BCryptPasswordEncoderWrapper;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoderWrapper();
    }

    @Bean
    public WebProperties.Resources resources(){
        return new WebProperties.Resources();
    }
}
