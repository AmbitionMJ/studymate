package com.example.weekdays.controller;


import com.example.weekdays.component.validator.CheckSignupValidator;
import com.example.weekdays.service.AccountService;
import com.example.weekdays.dto.SignupDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final CheckSignupValidator checkSignupValidator;

    @InitBinder("signupDto") //메서드가 어떤 객체에 대한 데이터 바인딩 및 유효성 검사를 처리할지 지정
    public void initBinder(WebDataBinder webDataBinder){ //이 메서드는 데이터 바인더를 커스터마이징 하는 역할입니다.
        webDataBinder.addValidators(checkSignupValidator); //checkSignupValidator 객체를 데이터 바인더에 추가합니다.

    }

    @GetMapping("/")
    public String main() {
        return "index";

    }

    @GetMapping("/signup") //회원가입 페이지
    public String signUpForm(Model model, SignupDto signupDto) { //회원가입 페이지에서 입력했던 정보들을 유지하기 위해서 파라미터를 정의합니다.
        model.addAttribute("signupDto", new SignupDto());
        return "account/signup";

    }

    @PostMapping("/signup") //회원가입 처리
    public String signUpSubmit(@Valid SignupDto signupDto, Errors errors, Model model) {

        if (errors.hasErrors()) { //에러가 있으면 입력 데이터를 유지하고 폼을 다시 보여줍니다.
            model.addAttribute("signupDto",signupDto);

            Map<String, String> validatorResult = accountService.validateHandling(errors); //유효성 통과 못한 필드와 메시지를 핸들링 합니다.
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "account/signup";
        }

        accountService.accountSave(signupDto);
        return "redirect:/";
    }

}