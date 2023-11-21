package com.matzip.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainBoardDto {

    private Long id;

    private String board_title;

    private String content;

    private String imgUrl;

    private Integer score;


    @QueryProjection
    public MainBoardDto(Long id, String board_title, String content, String imgUrl, Integer score){
        this.id = id;
        this.board_title = board_title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.score = score;
    }

}