package com.matzip.dto;

import com.matzip.entity.Feeling;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//상세 게시글에 필요한 감정표현 정보
public class FeelingBoardDtlDto {

    //내 감정표현
    private Feeling myFeeling;

    //게시글의 좋아요,싫어요 갯수
    private int likeCount;
    private int dislikeCount;



    public FeelingBoardDtlDto(Feeling myFeeling, int likeCount, int dislikeCount) {
        this.myFeeling = myFeeling;
        this.likeCount =likeCount;
        this.dislikeCount = dislikeCount;
    }
}