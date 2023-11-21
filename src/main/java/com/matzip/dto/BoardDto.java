package com.matzip.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardDto {

    private Long id;

    private String itemNm;

    private Integer price;

    private String itemDetail;

    /*여기는 BaseEntity*/

    private String sellStatCd;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}