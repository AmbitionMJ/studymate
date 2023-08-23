package com.example.weekdays.service;

import com.example.weekdays.domain.entity.Account;
import com.example.weekdays.domain.repository.AccountRepository;
import com.example.weekdays.dto.SignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    //회원가입 처리 하는 메서드
    public Account accountSave(@Valid SignupDto signupDto) {
        Account account = Account.builder()
                .email(signupDto.getEmail())
                .nickname(signupDto.getNickname())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .role(signupDto.getRole())
                .build();


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

    public void processNewAccount(SignupDto signupDto) { //회원가입 프로세스 처리하는 메서드
        Account newAccount = accountSave(signupDto); //signupDto를 사용하여 새로운계정을 생성합니다.
        newAccount.generateEmailCheckToken(); //newAccount 객체에 대해 이메일 확인을 위한 토큰을 생성합니다.
        sendSignUpConfirmEmail(newAccount); //이메일을 보냅니다.


    }

    public void sendSignUpConfirmEmail(Account newAccount) { //회원가입 인증 이메일을 생성하고 전송하는 메서드

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("회원 가입 인증");
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());
        javaMailSender.send(mailMessage); //메일 전송
    }

    public void completeSignUp(Account account) { //이메일인증 성공 여부, 시간
        account.completeSignUp();
    }


    public Account findByEmail(String email){ // db에서 email을 검색
        //controller에서 repository에 대한 직접적인 접근을 지양하기 위해 만들었습니다.
        return accountRepository.findByEmail(email);

    }
    public long count(){ //db에 저장된 모든 사용자 계정의 수를 세는 역할
        ////controller에서 repository에 대한 직접적인 접근을 지양하기 위해 만들었습니다.
        return accountRepository.count();

    }
}


