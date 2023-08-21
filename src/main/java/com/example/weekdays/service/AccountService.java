package com.example.weekdays.service;

import com.example.weekdays.domain.entity.Account;
import com.example.weekdays.domain.repository.AccountRepository;
import com.example.weekdays.dto.SignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;


    //회원가입 처리 하는 메서드, 비밀번호 암호화
    public Account accountSave(@Valid SignupDto signupDto) {
        signupDto.setRole("MEMBER");
        Account account = Account.builder()
                .email(signupDto.getEmail())
                .nickname(signupDto.getNickname())
                .password(passwordEncoder.encode(signupDto.getPassword())) //비밀번호 암호화
                .role(signupDto.getRole()).build();

        Account newAccount = accountRepository.save(account);

        newAccount.generateEmailCheckToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("회원 가입 인증");
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email="+newAccount.getEmail());

        javaMailSender.send(mailMessage);

        return accountRepository.save(account);


    }


    public Map<String, String> validateHandling(Errors errors) { //회원가입 시 유효성 체크

        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) { //유효성 검사에 실패한 필드 목록을 가져옵니다.
            String validKeyName = String.format("valid_%s", error.getField()); // 유효성 검사에 실패한 필드명을 가져옵니다.
            validatorResult.put(validKeyName, error.getDefaultMessage()); // 유효성 검사에 실패한 필드에 정의된 메시지를 가져옵니다.
        }
        return validatorResult;

    }

}


