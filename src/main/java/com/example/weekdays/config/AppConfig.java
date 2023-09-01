package com.example.weekdays.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Bean을 정의하고 구성하는 클래스입니다.
@Configuration
public class AppConfig {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();

        //자동 로그인을 구현하기 위함
        //AuthenticationManager를 사용하여 사용자를 로그인하고, 로그인 정보를 SecurityContextHolder에 저장하여 현재 사용자를
        //인증된 상태로 유지하는 방식으로 동작합니다.
    }

}
