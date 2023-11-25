package com.matzip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    //토큰검증 코드 때문에 entity와 변수명 같게 맞춰줘야함
    private String userid;
    private String user_pwd;
}
