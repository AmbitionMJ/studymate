package com.example.weekdays.domain.repository;

import com.example.weekdays.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface AccountRepository extends JpaRepository<Account,Long> {

boolean existsByEmail(String email); //이메일 존재 여부 확인
boolean existsByNickname(String nickname); //닉네임 존재 여부 확인


    Account findByEmail(String email); //email을 검색하는데 사용

    Account findByNickname(String nickname); //nickname을 검색하는데 사용
}
