package com.matzip.dto;

import com.matzip.entity.BoardImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class BoardImgDto {

    private Long id;

    ////안되면삭제
    private Long boardId; // 새로 추가될 boardId 필드

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();



    public static BoardImgDto of(BoardImg boardImg) {
        return modelMapper.map(boardImg,BoardImgDto.class);
    }

}