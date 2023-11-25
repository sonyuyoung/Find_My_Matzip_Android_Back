package com.matzip.config.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.matzip.config.oauth.PrincipalDetails;
import com.matzip.entity.Users;
import com.matzip.service.UsersService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Header에 토큰 있는지 확인

// Security Filter 중에서 BasicAuthenticationFilter 라는 것이 있다.
// 권한이나 인증이 필요한 특정 주소를 요청했을 경우 위의 필터를 무조건 타게 되어있다.
// 만약 권한이나 인증이 필요하지 않다면 위의 필터를 타지 않는다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private UsersService userService;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UsersService userService) {
		super(authenticationManager);
		this.userService = userService;
	}

	// 인증이나 권한이 필요한 주소요청이 있을 경우 해당 필터를 타게 된다.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String header = request.getHeader(JwtProperties.HEADER_STRING);
		System.out.println("jwtHeader : " + header);
		
		//header가 있는지 확인
		if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}

		//jwt토큰을 검증해서 정상적인 사용자인지 확인
		String token = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");

		//토큰에서 username추출 ->userId에 담음
		String userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
				.getClaim("username").asString();


		//정상적으로 들어옴
		if (userId != null) {
			//토큰의 userId 에 대응되는 user DB에서 찾음
			Users users = userService.findUsersById(userId);

			System.out.println("jwtHeader에 대응되는 userid : " + users.getUserid() );

			PrincipalDetails principalDetails = new PrincipalDetails(users);
			
			//JWT토큰 서명을 통해 서명이 정상이면 Authentication 객체 생성
			Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
					principalDetails.getAuthorities());

			//강제로 Security 세션에 접근해서 Authentication 객체 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		//체인 타게 됨
		chain.doFilter(request, response);
	}
}
