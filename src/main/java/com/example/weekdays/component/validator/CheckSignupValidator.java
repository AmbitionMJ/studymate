package com.example.weekdays.component.validator;

import com.example.weekdays.domain.repository.AccountRepository;
import com.example.weekdays.dto.SignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckSignupValidator extends AbstractValidator<SignupDto> { //extends object

    private final AccountRepository accountRepository;



    @Override
    protected void doValidate(SignupDto dto, Errors errors) {
        if(accountRepository.existsByEmail(dto.toEntity().getEmail())){
            //중복인 경우에는
            errors.rejectValue("email","아이디 중복","이미 사용중인 이메일 입니다.");

        }

        if(accountRepository.existsByNickname(dto.toEntity().getNickname())){
            errors.rejectValue("nickname","닉네임 중복","이미 사용중인 닉네임 입니다.");

        }
    }
}
