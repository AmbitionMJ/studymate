package com.example.weekdays.controller;


import com.example.weekdays.component.validator.CheckSignupValidator;
import com.example.weekdays.domain.entity.Account;
import com.example.weekdays.domain.repository.AccountRepository;
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
    public String signUpForm(Model model, SignupDto signupDto) { //회원가입 페이지에서 입력했던 정보들을 유지하기 위해서 파라미터를 정의합니다.
//        model.addAttribute("signupDto", new SignupDto());
        return "account/signup";

    }

    @PostMapping("/signup") //회원가입 처리
    public String signUpSubmit(@Valid SignupDto signupDto, Errors errors, Model model) {

        if (errors.hasErrors()) { //에러가 있으면 입력 데이터를 유지하고 폼을 다시 보여줍니다.
            model.addAttribute("signupDto", signupDto);

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
    public String checkEmailToken(String token, String email, Model model){ //회원가입 인증 이메일 토큰 확인 메서드
        Account account = accountService.findByEmail(email); //메일 주소로 등록된 계정 정보를 가져옵니다.
        if(account == null){ //검색된 계정이 없으면
            model.addAttribute("error", "wrong.email"); //오류 메시지를 모델에 추가
            return "account/checked-email"; //뷰에 에러메시지를 표시
        }

        if(!account.isValidToken(token)){ //검색된 계정의 토큰이 유효하지 않으면
            model.addAttribute("error","wrong.token"); //오류 메시지를 모델에 추가
            return "account/checked-email"; //뷰에 에러메시지를 표시

        }

        accountService.completeSignUp(account); // 토큰이 유효하면 인증여부를 true로 변경하고, 성공한 시간을 입력합니다.

        model.addAttribute("numberOfUser", accountService.count()); //count 메서드를 사용하여 현재 사용자 수를 보여줍니다.
        model.addAttribute("nickname", account.getNickname()); // 가입 완료된 닉네임을 뷰에 보여줍니다.
        return "account/checked-email";

    }


}


















