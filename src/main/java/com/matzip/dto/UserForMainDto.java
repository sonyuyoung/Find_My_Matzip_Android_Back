package com.matzip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForMainDto {
    private String userId;
    private String username;
    private String userImage;

    public UserForMainDto(String userId, String username, String userImage) {
        this.userId = userId;
        this.username = username;
        this.userImage = userImage;
    }
}
