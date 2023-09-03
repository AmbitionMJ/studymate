package com.example.weekdays.domain.entity;


import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="persistent_logins")
@Getter
public class PersistentLogins {

    @Id
    @Column(length = 64)
    private String series; // 식별

    @Column(nullable = false, length = 64)
    private String username; //사용자

    @Column(nullable = false, length = 64)
    private String token; //토큰 값

    @Column(name = "last_used", nullable = false, length = 64)
    private LocalDateTime lastUsed; //토큰의 마지막 사용 시간

}