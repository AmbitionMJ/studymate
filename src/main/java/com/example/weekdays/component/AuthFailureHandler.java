package com.example.weekdays.component;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String LoginErrorMsg = null;


        if (exception instanceof BadCredentialsException){
            LoginErrorMsg = "비밀번호가 정확하지 않습니다.";

        }
        else if (exception instanceof AuthenticationServiceException) {
            LoginErrorMsg = "존재하지 않는 계정입니다.";
        }

            request.setAttribute("LoginErrorMsg", LoginErrorMsg);
        request.getRequestDispatcher("/login").forward(request,response);


    }
}
