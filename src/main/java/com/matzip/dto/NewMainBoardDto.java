package com.matzip.dto;

import com.matzip.constant.BoardViewStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NewMainBoardDto {

    //새로 만들 메인에 이미지리스트 출력하기 위해서 새로 만든 DTO

    //1. 게시글(일반 데이터) 입력 부분

    //게시글아이디
    private Long id;

    //식당아이디
    private String resId;

    //유저아이디
    private String modifiedBy;

    //유저정보가져오기
    private UserForMainDto user;

    //일단 에러나니까추가해봄
    private BoardViewStatus boardViewStatus;

    //게시글제목
    @NotBlank(message = "제목을 입력 바랍니다.")
    private String board_title;

    //리뷰상세내역
    private String content;

    //리뷰 평점
    @NotNull(message = "평점을 입력 바랍니다.")
    private Integer score;

    //2. 리뷰 이미지 입력 부분
    private List<BoardImgDto> boardImgDtoList;

//    @QueryProjection
//    public NewMainBoardDto(Long id, String resId, String modifiedBy, BoardViewStatus boardViewStatus,
//                           String board_title, String content, Integer score, List<BoardImgDto> boardImgDtoList) {
//        this.id = id;
//        this.resId = resId;
//        this.modifiedBy = modifiedBy;
//        this.boardViewStatus = boardViewStatus;
//        this.board_title = board_title;
//        this.content = content;
//        this.score = score;
//        this.boardImgDtoList = boardImgDtoList;
//    }

    //유저정보까지 들고오기
    @QueryProjection
    public NewMainBoardDto(Long id, String resId, String modifiedBy, BoardViewStatus boardViewStatus,
                           String board_title, String content, Integer score, List<BoardImgDto> boardImgDtoList, UserForMainDto user) {
        this.id = id;
        this.resId = resId;
        this.modifiedBy = modifiedBy;
        this.boardViewStatus = boardViewStatus;
        this.board_title = board_title;
        this.content = content;
        this.score = score;
        this.boardImgDtoList = boardImgDtoList;
        this.user = user;
    }
}
