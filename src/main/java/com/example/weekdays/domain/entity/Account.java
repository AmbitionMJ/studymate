package com.example.weekdays.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") //id만 사용하도록 한다.
@Getter
@Setter
@Entity(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB AI 설정
    private Long id;

    @Column(length = 20, nullable = false) //이메일
    private String email;

    @Column(length = 20, nullable = false) //닉네임
    private String nickname;

    @Column(length = 300, nullable = false) //비밀번호
    private String password;

    @Column(length = 10) //권한
    private String role;


    private boolean emailVerified; //이메일 인증 여부
    private String emailCheckToken; //토큰 값을 매칭하기 위한 필드
    private LocalDateTime joinedAt; //언제 인증을 했는지
    private LocalDateTime emailCheckTokenGeneratedAt;

    private String bio; //자기소개
    private String url; //웹사이트 url
    private String occupation; //직업
    private String location; //지역

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileImage; //프로필 이미지

    private boolean keywordCreatedByEmail; //알림을 이메일로 받는다.
    private boolean keywordCreatedByWeb; //알림을 웹으로 받는다.

    @Builder
    public Account(Long id, String email, String nickname, String password, String role,
                        String emailCheckToken, LocalDateTime joinedAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.emailCheckToken = emailCheckToken;
        this.role =role;
        this.joinedAt = joinedAt;

    }

    public void generateEmailCheckToken(){ //토큰 발행
        this.emailCheckToken = UUID.randomUUID().toString();

    }

    public void completeSignUp() { //이메일인증 성공 여부, 시간
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();

    }

    public boolean isValidToken(String token) { // 토큰 비교
        return this.emailCheckToken.equals(token);

    }


}
