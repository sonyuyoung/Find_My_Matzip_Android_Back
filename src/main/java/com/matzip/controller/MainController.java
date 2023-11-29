package com.matzip.controller;

import com.matzip.dto.BoardSearchDto;
import com.matzip.dto.MainBoardDto;
import com.matzip.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
//@RestController
@RequiredArgsConstructor
public class MainController {

    private final BoardService boardService;

//    메인 페이저블
//    @GetMapping(value = "/pagerbleMain")
//    public String main(BoardSearchDto boardSearchDto, Optional<Integer> page, Model model){
//
//        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
//        Page<MainBoardDto> boards = boardService.getMainBoardPage(boardSearchDto, pageable);
//
//        model.addAttribute("boards", boards);
//        model.addAttribute("boardSearchDto", boardSearchDto);
//        model.addAttribute("maxPage", 5);
//
//        return "main";
//    }

//    메인 페이저블(rest)
    @GetMapping(value = "/pagerbleMain")
    public ResponseEntity<List<MainBoardDto>> getMainBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            BoardSearchDto boardSearchDto
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MainBoardDto> boards = boardService.getMainBoardPage(boardSearchDto, pageable);

        return ResponseEntity.ok(boards.getContent());
    }

    @GetMapping(value = "/pagerbleFollowerMain")
    public ResponseEntity<List<MainBoardDto>> getFollowerMainBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            BoardSearchDto boardSearchDto
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MainBoardDto> boards = boardService.getFollowerMainBoards(boardSearchDto, pageable);

        return ResponseEntity.ok(boards.getContent());
    }


    //페이저블없이 그냥 다 끌어오기
//    @GetMapping(value = "/")
//    public String main(BoardSearchDto boardSearchDto, Model model){
//        List<MainBoardDto> boards = boardService.getMainBoard(boardSearchDto);
//        model.addAttribute("boards", boards);
//        return "main";
//    }

//    //전체 게시글 목록 조회(Rest)
//    @GetMapping(value = "/")
//    public List<MainBoardDto> main(BoardSearchDto boardSearchDto) {
//        return boardService.getMainBoard(boardSearchDto);
//    }

    //전체 게시글 목록 조회(Rest)
    @GetMapping(value = "/")
    @ResponseBody
    public List<MainBoardDto> getAllBoards(BoardSearchDto boardSearchDto) {
        return boardService.getMainBoard(boardSearchDto);
    }


}