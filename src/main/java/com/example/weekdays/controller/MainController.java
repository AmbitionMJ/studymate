package com.example.weekdays.controller;


import com.example.weekdays.component.UserAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {


    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserAccount userAccount , Model model){
        //spring security에서 생성된 커스텀 userDetails 객체입니다.
        //엔터티를 직접적으로 참조하지 않고, 사용자 정보를 포함하고 있습니다.

        if(userAccount != null){

            model.addAttribute("userAccount", userAccount);


        }
        return "index";
    }

}
