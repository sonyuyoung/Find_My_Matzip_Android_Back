package com.matzip.dto;

import com.matzip.constant.BoardViewStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RestaurantSearchDto {

    //게시글 데이터 조회 시, 게시글 조회 조건 -> BoardRepositoryCustom으로 이동 후 사용자 정의 인터페이스를 작성
    private String searchDateType;

    private String searchBy;

    private String searchQuery = "";

}