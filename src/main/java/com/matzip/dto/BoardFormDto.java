package com.matzip.dto;

import com.matzip.constant.BoardViewStatus;
import com.matzip.entity.Board;
import com.matzip.entity.Restaurant;
import com.matzip.entity.Users;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

//폼에서 입력한 내용(게시글 + 이미지) BoardFormDto에 담아서
// -> BoardController로 이동
@Getter @Setter
public class BoardFormDto {

    //1. 게시글(일반 데이터) 입력 부분

    //게시글아이디
    private Long id;

    //식당아이디
    private String resId;

    //유저아이디
    private String user_id;

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
    private List<BoardImgDto> boardImgDtoList = new ArrayList<>();

    private List<Long> boardImgIds = new ArrayList<>();


    //board객체를 boardFormDto로 변환 : 일반데이터만
    public BoardFormDto(Board board){
        this.id = board.getId();
        this.resId = board.getResId().getResId();
        this.user_id = board.getCreatedBy();
        this.board_title = board.getBoard_title();
        this.content = board.getContent();
        this.score = board.getScore();
    }

    public BoardFormDto() {

    }
}