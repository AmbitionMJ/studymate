package com.example.weekdays.component.validator;


import com.example.weekdays.dto.PasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckPasswordValidator extends AbstractValidator<PasswordDto> {


    @Override
    protected void doValidate(PasswordDto dto, Errors errors) {
        if (!dto.getNewPassword().equals(dto.getNewPasswordConfirm())) {
            errors.rejectValue("newPassword", "패스워드 불일치", "패스워드를 재확인 해주세요.");

        }
    }
}