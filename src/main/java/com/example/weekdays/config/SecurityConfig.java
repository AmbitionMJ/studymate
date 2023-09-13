package com.example.weekdays.config;


import com.example.weekdays.component.AuthFailureHandler;
import com.example.weekdays.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthFailureHandler authFailureHandler;
    private final AccountService accountService;
    private final DataSource dataSource;




    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().antMatchers("/vendors/**","/build/**","/images/**","/node_modules/**","/error");
        //static으로 들어오는 요청 무시

    }


    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public  SecurityFilterChain config(HttpSecurity http) throws Exception{

        http.authorizeRequests()
                .antMatchers("/","/signup","/check-email-token","/login","/profile","/password/update","/profile/update","/account/notifications"
                ,"/find-password")
                .permitAll().
                anyRequest().authenticated();


        //로그인 설정
        http.formLogin()
                .loginPage("/login").permitAll() //커스텀한 로그인 페이지로
                .defaultSuccessUrl("/") //로그인 성공 후 메인으로
                .failureHandler(authFailureHandler);
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/") //로그아웃 하면 메인으로
                .invalidateHttpSession(true); //세션 무효화

        http.rememberMe()
                .userDetailsService(accountService) //사용자 정보를 가져옵니다.
                .tokenRepository(tokenRepository()); // 사용자의 RememberMe 토큰을 저장하는데 사용할 토큰 리포지토리를 설정합니다.

        return http.build();
    }

        @Bean
        public PersistentTokenRepository tokenRepository(){
            JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
            jdbcTokenRepository.setDataSource(dataSource); //데이터베이스 연결을 설정합니다.

            return jdbcTokenRepository;
    }

    

}

