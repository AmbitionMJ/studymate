package com.example.weekdays.controller;


import com.example.weekdays.component.UserAccount;
import com.example.weekdays.dto.BoardDto;
import com.example.weekdays.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    private final BoardService boardService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserAccount userAccount , Model model, Long id){
        //spring security에서 생성된 커스텀 userDetails 객체입니다.
        //엔터티를 직접적으로 참조하지 않고, 사용자 정보를 포함하고 있습니다.

        if(userAccount != null){

            model.addAttribute("userAccount", userAccount);

        }

        List<BoardDto> boardDtoList = boardService.postList(id);
        model.addAttribute("boardDtoList",boardDtoList);
        return "index";
    }

}
