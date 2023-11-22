package com.example.weekdays.domain.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") //id만 사용하도록 한다.
@Getter
@Setter
@Entity(name = "account")
public class Account extends Time implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB AI 설정
    private Long id;

    @Column(length = 20, nullable = false) //이메일
    private String email;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

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
        this.emailCheckTokenGeneratedAt =LocalDateTime.now();

    }

    public void completeSignUp() { //이메일인증 성공 여부, 시간
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();

    }

    public boolean isValidToken(String token) { // 토큰 비교
        return this.emailCheckToken.equals(token);

    }
    public boolean canSendConfirmEmail() { //시간 텀을 정해줍니다.
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한을 설정합니다. 여기에서는 하나의 권한을 부여하도록 예제로 구현합니다.
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+role));
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return emailVerified;
    }
}
