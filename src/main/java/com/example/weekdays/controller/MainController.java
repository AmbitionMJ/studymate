package com.example.weekdays.controller;


import com.example.weekdays.component.UserAccount;
import com.example.weekdays.dto.BoardDto;
import com.example.weekdays.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    private final BoardService boardService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserAccount userAccount, Model model,
                       Pageable pageable,
                       @RequestParam(required = false) String option,
                       @RequestParam(required = false) String keyword) {
        //spring security에서 생성된 커스텀 userDetails 객체입니다.
        //엔터티를 직접적으로 참조하지 않고, 사용자 정보를 포함하고 있습니다.
        if (userAccount != null) {
            model.addAttribute(userAccount);
        }// 인증된 사용자가 있을 때만 정보를 보여줍니다.

        if(option == null || option.isEmpty()){
            option = "titleAndContent";

        }

        Page<BoardDto> boardDtoPage;
        if(keyword != null && !keyword.isEmpty()){
            boardDtoPage = boardService.searchPosts(keyword, pageable, option);

        }else {
            boardDtoPage = boardService.getPostPage(pageable);

        }
        model.addAttribute("boardDtoPage",boardDtoPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("option", option);

        return "index";

    }

}
