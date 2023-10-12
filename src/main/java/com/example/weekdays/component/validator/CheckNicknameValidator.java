package com.example.weekdays.component.validator;


import com.example.weekdays.domain.repository.AccountRepository;
import com.example.weekdays.dto.NicknameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckNicknameValidator extends AbstractValidator<NicknameDto>{

    private final AccountRepository accountRepository;

    @Override
    protected void doValidate(NicknameDto dto, Errors errors) {
        if(accountRepository.existsByNickname(dto.getNickname())){
            errors.rejectValue("nickname","닉네임 중복","이미 사용중인 닉네임 입니다.");

        }
    }
}
