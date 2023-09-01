package com.example.weekdays.component;


import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //런타임 시에 유지되도록 설정
@Target(ElementType.PARAMETER) //메서드의 파라미터에만 적용될 수 있음
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
//

public @interface CurrentUser {



}
