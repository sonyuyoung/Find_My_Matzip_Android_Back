package com.matzip.controller;

import com.matzip.dto.UsersFormDto;
import com.matzip.entity.Users;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String status;
    private String message;
    private UsersFormDto loginUser;
}
