package com.example.weekdays.controller;


import com.example.weekdays.component.UserAccount;
import com.example.weekdays.component.validator.CheckNicknameValidator;
import com.example.weekdays.component.validator.CheckPasswordValidator;
import com.example.weekdays.component.validator.CheckSignupValidator;
import com.example.weekdays.dto.*;
import com.example.weekdays.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Controller
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final CheckSignupValidator checkSignupValidator;
    private final CheckPasswordValidator checkPasswordValidator;
    private final CheckNicknameValidator checkNicknameValidator;


    @InitBinder("signupDto") //메서드가 어떤 객체에 대한 데이터 바인딩 및 유효성 검사를 처리할지 지정
    public void initBinder(WebDataBinder webDataBinder) { //이 메서드는 데이터 바인더를 커스터마이징 하는 역할입니다.
        webDataBinder.addValidators(checkSignupValidator); //checkSignupValidator 객체를 데이터 바인더에 추가합니다.

    }

    @InitBinder("passwordDto")
    public void passwordBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(checkPasswordValidator);

    }

    @InitBinder("nicknameDto")
    public void nicknameBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(checkNicknameValidator);

    }

    @GetMapping("/signup") //회원가입 페이지
    public String signUpForm(Model model, SignupDto signupDto) {
        return "account/signup";

    }

    @GetMapping("/login") //로그인 페이지
    public String loginForm() {
        return "account/login";

    }

    @PostMapping("/login")
    public String loginError(Model model) { //로그인 처리
        model.addAttribute("loginErrorMsg");
        return "account/login";

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
        if ("wrong.email".equals(verification)) {
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


    @GetMapping("/check-email") //메일 재전송 페이지
    public String checkEmail(@AuthenticationPrincipal UserAccount userAccount, Model model) {

        model.addAttribute("email", userAccount.getAccount().getEmail());

        return "account/check-email";

    }

    @PostMapping("/check-email") //메일 재전송 처리
    public String resendConfirmEmail(@AuthenticationPrincipal UserAccount userAccount, Model model) {
        if (!userAccount.getAccount().canSendConfirmEmail()) { //마지막으로 발행한 토큰 시간과 3분의 텀이 있는지 체크
            model.addAttribute("error", "인증 이메일은 3분에 한번만 전송할 수 있습니다.");
            model.addAttribute("email", userAccount.getAccount().getEmail());

            return "account/check-email";

        }
        accountService.updateTokenAndResendMail(userAccount);

        return "redirect:/"; //화면 리프레시 할때마다 메일을 계속 보낼 수 있으니 리다이렉트로 설정

    }


    @GetMapping("/profile/{nickname}") //프로필 페이지
    public String profileForm(@PathVariable String nickname, Model model, @AuthenticationPrincipal UserAccount userAccount) {

        boolean isOwner = nickname.equals(userAccount.getAccount().getNickname());

        if (!isOwner) {

            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");

        }

        model.addAttribute("userAccount", userAccount);
        model.addAttribute("isOwner", isOwner);

        return "account/profile";
    }


    @GetMapping("/password/update") //비밀번호 수정 페이지

    public String passwordUpdateForm(@AuthenticationPrincipal UserAccount userAccount, Model model) {

        model.addAttribute("userAccount", userAccount);
        model.addAttribute("passwordDto", new PasswordDto()); //PasswordDto 클래스의 생성자를 호출하여 새로운 PasswordDto 객체를 만들고 할당합니다.

        return "account/password-update";
    }

    @PostMapping("/password/update") //비밀번호 수정 처리
    public String passwordUpdate(@AuthenticationPrincipal UserAccount userAccount, @Valid PasswordDto passwordDto,
                                 Errors errors, Model model, RedirectAttributes attributes) {


        if (errors.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            return "account/password-update";
        }

        accountService.updatePassword(userAccount.getAccount(), passwordDto.getNewPassword());
        attributes.addFlashAttribute("message", "수정이 완료되었습니다.");


        return "redirect:/password/update";
    }

    @GetMapping("/profile/update") //프로필 수정 페이지
    public String profileUpdateForm(@AuthenticationPrincipal UserAccount userAccount, Model model) {
        model.addAttribute("userAccount", userAccount);
        model.addAttribute(new ProfileDto(userAccount));


        return "account/profile-update";


    }

    @PostMapping("/profile/update") //프로필 수정 처리
    public String profileUpdate(@AuthenticationPrincipal UserAccount userAccount, @Valid ProfileDto profileDto,
                                Errors errors, Model model, RedirectAttributes attributes) {

        if (errors.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            return "account/profile-update";
        }

        accountService.updateProfile(userAccount.getAccount(), profileDto);


        attributes.addFlashAttribute("message", "수정이 완료되었습니다.");
        return "redirect:/profile/update";
    }


    @GetMapping("/account/update") //닉네임 수정 페이지
    public String accountUpdateForm(@AuthenticationPrincipal UserAccount userAccount, Model model, Principal principal) {
        model.addAttribute("userAccount", userAccount);
        model.addAttribute("nicknameDto", new NicknameDto());
        return "account/account-update";

    }

    @PostMapping("/account/update") //닉네임 수정 처리
    public String accountUpdate(@AuthenticationPrincipal UserAccount userAccount, @Valid NicknameDto nicknameDto, Errors errors, Model model, RedirectAttributes attributes) {

        if (errors.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            return "account/account-update";

        }
        accountService.updateNickname(userAccount.getAccount(), nicknameDto.getNickname());

        attributes.addFlashAttribute("message", "수정이 완료되었습니다.");
        return "redirect:/account/update";

    }


    @GetMapping("/notifications") //알림 설정 페이지
    public String updateNotificationsForm(@AuthenticationPrincipal UserAccount userAccount, Model model) {

        model.addAttribute(userAccount);
        model.addAttribute(new NotificationsDto(userAccount.getAccount()));

        return "account/notifications";

    }

    @PostMapping("/notifications") //알림 설정 처리
    public String updateNotification(@AuthenticationPrincipal UserAccount userAccount, NotificationsDto notificationsDto, Errors errors, Model model, RedirectAttributes attributes) {


        if (errors.hasErrors()) {
            model.addAttribute(userAccount);
            return "account/notifications";
        }

        accountService.updateNotifications(userAccount.getAccount(), notificationsDto);
        attributes.addFlashAttribute("message", "수정이 완료되었습니다.");

        return "redirect:/notifications";


    }

    @GetMapping("/find-password") //비밀번호 찾기 페이지
    public String emailLoginForm() {


        return "account/find-password";

    }

    @PostMapping("/find-password") //비밀번호 찾기 처리 (임시비밀번호 발급)
    public String temporaryPassword(String email, Model model, RedirectAttributes attributes) {

        String verification = accountService.resetPasswordByEmail(email); //검증을 수행합니다.
        if ("wrong.email".equals(verification)) {
            model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
            return "account/find-password";

        }
        if ("time.yet".equals(verification)) {
            model.addAttribute("error", "임시비밀번호 발급은 3분마다 시도할 수 있습니다.");

            return "account/find-password";
        }

        attributes.addFlashAttribute("message", "임시 비밀번호를 발급하였습니다. 로그인 후 반드시 비밀번호를 변경해주세요.");
        return "redirect:/find-password";
    }


}


















