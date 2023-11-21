package com.matzip.controller;

import com.matzip.dto.BoardSearchDto;
import com.matzip.dto.MainBoardDto;
import com.matzip.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardService boardService;

    @GetMapping(value = "/")
    public String main(BoardSearchDto boardSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainBoardDto> boards = boardService.getMainBoardPage(boardSearchDto, pageable);

        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

}