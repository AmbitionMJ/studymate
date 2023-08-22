package com.example.weekdays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class WeekdaysApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원 가입 화면 테스트")
    @Test
    void SignUpForm() throws Exception{
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/signup"))
                .andExpect(model().attributeExists("signupDto"));

    }


    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_wrong_input() throws Exception{
        mockMvc.perform(post("/signup")
                .param("nickname","MJ")
                .param("email","asd@asd.asd")
                .param("password","12345678")
                .with(csrf())) //thymeleaf에서는 자동으로 csrf 토큰을 생성하지만 테스트코드에서는 별도로 적어야한다.
                .andExpect(status().isOk())
                .andExpect(view().name("account/signup"));

    }
}
