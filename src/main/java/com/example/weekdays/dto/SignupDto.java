package com.example.weekdays.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignupDto { //회원가입 시 이메일, 닉네임, 비밀번호, 권한 전송을 위한 객체

    private Long id;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,8}$", message = "닉네임은 특수문자를 제외한 2~8자리로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Pattern(regexp = ".{8,20}", message = "비밀번호는 8자 이상 20자 이내로 입력해주세요.")
    private String password;

    private String role;

}
