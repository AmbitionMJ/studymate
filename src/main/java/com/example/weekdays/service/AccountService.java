package com.example.weekdays.service;

import com.example.weekdays.component.UserAccount;
import com.example.weekdays.domain.entity.Account;
import com.example.weekdays.domain.repository.AccountRepository;
import com.example.weekdays.dto.NotificationsDto;
import com.example.weekdays.dto.PasswordDto;
import com.example.weekdays.dto.ProfileDto;
import com.example.weekdays.dto.SignupDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final ModelMapper modelMapper;



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


    public Account findByEmail(String email) { // db에서 email을 검색
        //controller에서 repository에 대한 직접적인 접근을 지양하기 위해 만들었습니다.
        return accountRepository.findByEmail(email);

    }


    public String checkEmailToken(String token, String email) { //이메일 주소와 토큰을 이용하여 이메일 인증을 처리합니다.

        Account account = accountRepository.findByEmail(email); //주어진 이메일 주소로 repository에서 해당 이메일 주소와 일치하는 계정을 검색합니다.
        if (account == null) { //검색된 계정이 없으면
            return "wrong.email"; //wrong.email 문자열을 반환

        }
        if (!account.isValidToken(token)) { //검색된 계정이 존재하면 토큰 유효성을 검사하고 토큰이 유효하지 않으면 wrong.token 문자열을 반환
            return "wrong.token";
        }
        account.completeSignUp(); //email과 token 값을 만족하면 인증여부를 true로 바꾸고 가입완료한 시간을 업데이트 합니다.

        accountRepository.save(account);
        return account.getNickname();//가입이 완료된 계정의 닉네임을 반환합니다.
    }



    //spring security 로그인 필수로 구현해야하는 메소드
    //loadUserByUsername함수에서 id로 DB조회
    //찾을 수 없으면 UsernameNotFoundException을 Throw 합니다.
    @Transactional(readOnly = true) //데이터를 읽어오는 용도이기 때문에 readOnly를 주었고, 그에 따라 성능에 유리
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByEmail(username);
        if(account.getEmail() == null) {
            throw new UsernameNotFoundException(username); //username(email)이 잘못됐다고 예외를 던져준다.
        }
        if(account.getPassword() == null) {
            throw new BadCredentialsException(username); //비밀번호 틀렸을 시

        }
        return new UserAccount(account);
        //DB에 들어있는 email을 username으로 취급해서 유저를 읽어옵니다.
        //인증 처리가 되면 authentication이라는 객체를 만들어서 securityContextHolder에 넣어줍니다.

    }

    public void updateTokenAndResendMail(UserAccount userAccount){ //이메일 재전송 하기 위해서 사용합니다.
        Account account =  userAccount.getAccount();
        account.generateEmailCheckToken(); //토큰을 발행하고 발행한 시간을 update
        sendSignUpConfirmEmail(account); // 메일 전송

        accountRepository.save(account);

    }


    public void updateProfile(Account account, ProfileDto profileDto){ // 프로필 소개란 수정
        modelMapper.map(profileDto,account);
        accountRepository.save(account);

    }


    public void updatePassword(Account account, String newPassword){ //비밀번호 수정
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

    }
    public void updateNotifications(Account account, NotificationsDto notificationsDto) { //알림 설정 수정
        modelMapper.map(notificationsDto,account);
        accountRepository.save(account);

    }


}