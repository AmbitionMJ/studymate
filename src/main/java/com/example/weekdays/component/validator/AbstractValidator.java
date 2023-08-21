package com.example.weekdays.component.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
public abstract class AbstractValidator<T> implements Validator {
// 중복검사 공통 로직 하위클래스에서 재사용 가능
    @Override
    public boolean supports(Class<?> clazz) { //검사를 수행할 대상 클래스를 지정하는 역할
        return true; //모든 클래스에 대한 검증을 지원할거라서 true를 반환했다.
    }

    @SuppressWarnings("unchecked")  //컴파일러에서 경고하지 않도록 하기 위해 사용
    @Override
    public void validate(Object target, Errors errors) {
        try {
            doValidate((T) target, errors);
        } catch (RuntimeException e) {
            log.error("중복 검증 에러", e);
            throw e;
        }
    }
    //유효성 검증 로직
    protected abstract void doValidate(final T dto, final Errors errors); //하위 클래스에서 구현해야 한다.
    //실제 유효성 검사 로직을 구현. T 타입의 DTO  객체와 Errors 객체를 받아서 유효성 검사를 수행합니다.
}