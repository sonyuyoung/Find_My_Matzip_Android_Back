package com.matzip.dto;

import com.matzip.constant.BoardViewStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BoardFormNoImgDto {
    //1. 게시글(일반 데이터) 입력 부분

    //게시글아이디
    private String userId;


    //유저아이디
    private String boardViewStatus;

    private String boardTitle;

    //리뷰상세내역
    private String content;

    private int score;
}
