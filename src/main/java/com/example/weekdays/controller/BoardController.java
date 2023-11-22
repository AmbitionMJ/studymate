package com.example.weekdays.controller;

import com.example.weekdays.component.UserAccount;
import com.example.weekdays.dto.BoardDto;
import com.example.weekdays.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/write") //게시글 등록 페이지
    public String boardWriteForm(@AuthenticationPrincipal UserAccount userAccount, Model model){


        if(userAccount != null){
            model.addAttribute("userAccount", userAccount);

        }

        return "board/board-write";
    }

    @PostMapping("/board/write") //게시글 등록 처리
    public String boardWrite(@AuthenticationPrincipal UserAccount userAccount, BoardDto boardDto, Model model){

        boardService.createBoard(boardDto);
        model.addAttribute(userAccount);

        return "redirect:/";
    }

    @GetMapping("/board/detail/{id}") //게시글 상세보기 페이지
    public String boardDetailForm(@PathVariable Long id, @AuthenticationPrincipal UserAccount userAccount, Model model){

        if(userAccount !=null){
            model.addAttribute("userAccount",userAccount);

        }

        BoardDto boardDto = boardService.getPostDetail(id);
        model.addAttribute("boardDto",boardDto);

        return "board/board-detail";

    }
    
    

}
