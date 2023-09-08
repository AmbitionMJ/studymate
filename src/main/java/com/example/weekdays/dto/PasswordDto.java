package com.example.weekdays.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class PasswordDto {


    @Length(min = 8, max = 30)
    private String newPassword; // 새 비밀번호

    @Length(min = 8, max = 30)
    private String newPasswordConfirm; //새 비밀번호 확인


}