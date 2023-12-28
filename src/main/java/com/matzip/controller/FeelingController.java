package com.matzip.controller;

import com.matzip.service.FeelingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FeelingController {
    private final FeelingService feelingService;


    //좋아요 & 싫어요
    @GetMapping("/setFeeling/{boardId}/{newFeel}")
    public @ResponseBody ResponseEntity<Map<String, Object>> setFeeling(@PathVariable Long boardId,@PathVariable int newFeel, Principal principal) {
        System.out.println("감정표현 추가 전 ");

        //result => -1:싫어요, 0:취소, +1:좋아요
        Integer data = feelingService.setFeeling(boardId,newFeel, principal.getName());

        System.out.println("감정표현 추가 후 ");

        Map<String,Object> result = new HashMap<>();
        result.put("data",data);
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }

    //게시글의 좋아요 수
    @GetMapping("/countLike/{boardId}")
    public @ResponseBody int countLike(@PathVariable Long boardId) {
        return feelingService.countFeeling(boardId,1);
    }

    //게시글의 싫어요 수
    @GetMapping("/countDislike/{boardId}")
    public @ResponseBody int countDislike(@PathVariable Long boardId) {
        return feelingService.countFeeling(boardId,-1);
    }

}