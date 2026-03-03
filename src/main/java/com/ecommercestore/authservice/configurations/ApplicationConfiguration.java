package com.ecommercestore.authservice.configurations;

import com.ecommercestore.authservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {

    @Bean
//    @Primary
    public IAuthService authService(
            @Value("${authServiceType}") String authServiceType,
            @Qualifier("localAuthService") IAuthService localAuthService) {
        return localAuthService;
    }

    @Bean
    public BCryptPasswordEncoder getBcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public String getEncodedKey(@Value("${passedEncodedKey}") String encodedKey) {
        return encodedKey;
    };
}
