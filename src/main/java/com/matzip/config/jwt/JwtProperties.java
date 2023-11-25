package com.matzip.config.jwt;

public interface JwtProperties {

	// 암호화 키 
	String SECRET = "matzip";
	// 토큰 만료시간 단위 :  ms 
	int EXPIRATION_TIME = 86400000;
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";

	// 인증 타입
	/*
	 * 	일반적으로 토큰은 요청 헤더의 Authorization 필드에 담아져 보내집니다.
	Authorization: <type> <credentials>
	
	Basic
	사용자 아이디와 암호를 Base64로 인코딩한 값을 토큰으로 사용한다. (RFC 7617)

	Bearer
	JWT 혹은 OAuth에 대한 토큰을 사용한다. (RFC 6750)
	

	*/
}
