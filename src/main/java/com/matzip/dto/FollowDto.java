package com.matzip.dto;

import com.matzip.entity.Follow;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDto {

    //user 정보
    private String id;
    private String name;
    private String profileImage;

    //(로그인한 유저A가) ->  (pageUsers의 팔로워를) 본인이 팔로우 했는지 여부 체크
    private boolean  subscribeState;

    //팔로워
    public FollowDto(Follow follow) {
        this.id = follow.getFromUser().getUserid();
        this.name = follow.getFromUser().getUsername();
        this.profileImage = follow.getFromUser().getUser_image();
        this.subscribeState = false;
    }

    //팔로잉
    public FollowDto(String fol_id, String fol_name ,String fol_profileImage) {
        this.id = fol_id;
        this.name =fol_name;
        this.profileImage = fol_profileImage;
        this.subscribeState = false;
    }
}