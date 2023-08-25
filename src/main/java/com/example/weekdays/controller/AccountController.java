package com.example.weekdays.controller;


import com.example.weekdays.component.validator.CheckSignupValidator;
import com.example.weekdays.domain.entity.Account;
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
    public void initBinder(WebDataBinder webDataBinder) { //이 메서드는 데이터 바인더를 커스터마이징 하는 역할입니다.
        webDataBinder.addValidators(checkSignupValidator); //checkSignupValidator 객체를 데이터 바인더에 추가합니다.

    }

    @GetMapping("/")
    public String main() {
        return "index";

    }

    @GetMapping("/signup") //회원가입 페이지
    public String signUpForm(Model model, SignupDto signupDto) {
        return "account/signup";

    }

    @PostMapping("/signup") //회원가입 처리
    public String signUpSubmit(@Valid SignupDto signupDto, Errors errors, Model model) {

        if (errors.hasErrors()) { //에러가 있으면 입력 데이터를 유지하고 폼을 다시 보여줍니다. Spring MVC 자체 처리
            Map<String, String> validatorResult = accountService.validateHandling(errors); //유효성 통과 못한 필드와 메시지를 핸들링 합니다.
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));

            }

            return "account/signup";
        }
        accountService.processNewAccount(signupDto);
        return "redirect:/";
    }


    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {

        String verification = accountService.checkEmailToken(token, email); //메서드를 호출하여 이메일 확인 토큰을 검증하고 결과를 문자열로 반환
        if ("wrong.email".equals(verification)) { //
            model.addAttribute("error", "잘못된 이메일입니다.");
            return "account/checked-email";

        } else if ("wrong.token".equals(verification)) {
            model.addAttribute("error", "유효하지 않은 토큰입니다.");
            return "account/checked-email";

        } else {
            model.addAttribute("nickname", verification);

        }

        return "account/checked-email";
    }

}


















