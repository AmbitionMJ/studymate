package com.example.weekdays.component;


import com.example.weekdays.domain.entity.Account;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAccount extends User { //User클래스는 SpringSecurity에서 제공하는 사용자 정보를 나타내는 클래스입니다.



    private final Account account;

    public UserAccount(Account account){ //커스터마이징
        super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority("MEMBER")));
        this.account = account;
    }  //User클래스의 생성자는 사용자의 이름, 비밀번호, 권한 목록 등을 매개변수로 받아 사용자를 생성합니다.


}
