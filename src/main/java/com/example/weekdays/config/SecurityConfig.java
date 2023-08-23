package com.example.weekdays.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().antMatchers("/vendors/**","/build/**");
        //static으로 들어오는 요청 무시

    }


    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public  SecurityFilterChain config(HttpSecurity http) throws Exception{

        http.authorizeRequests()
                .antMatchers("/","/signup","/check-email-token")
                .permitAll().
                anyRequest().authenticated();


        return http.build();
    }
    

    

}

