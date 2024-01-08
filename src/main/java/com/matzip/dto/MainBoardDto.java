package com.matzip.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MainBoardDto {

    private Long id;

    private String board_title;

    private String content;

    private String imgUrl;

    private Integer score;

    private List<CommentDto> comments;
    @QueryProjection
    public MainBoardDto(Long id, String board_title, String content, String imgUrl, Integer score) {
        this.id = id;
        this.board_title = board_title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.score = score;
    }
    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

}