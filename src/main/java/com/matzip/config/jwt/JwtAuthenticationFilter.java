package com.matzip.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.config.oauth.PrincipalDetails;
import com.matzip.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {	
	
	private final AuthenticationManager authenticationManager;

	//(/login로 POST 요청시 동작)
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter 진입(인증 시도)");

		ObjectMapper om = new ObjectMapper();
		LoginDto jwtUser = null;
		
		//jwt로 받은 data 형태 변환
		try {
			//"JSON" -> "Java Object" (아직 자동 변환 전이라서 직접 변환)
			jwtUser = om.readValue(request.getInputStream(), LoginDto.class);
		} catch(Exception e) {
			e.printStackTrace();
		}

		System.out.println("JwtAuthenticationFilter(jwtUser):  "+jwtUser.getUserid()+","+jwtUser.getUser_pwd());

		//Authentication 상속 , spring security에 인증 완료 결과를 전달해주는 구현체
		//상속 구조 : Principal -> Athentication -> AbstractAuthenticationToken -> UsernamePasswordAuthenticationToken
		//로그인 인증 후 사용자 정보를 세션 등에 따로 보관해두지 않았기때문에 꺼내 쓸 수 있는 객체는 결국 이거.
		
		//토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(
						jwtUser.getUserid(),
						jwtUser.getUser_pwd());
		
		System.out.println("JwtAuthenticationFilter  =--> "
				+ " UsernamePasswordAuthenticationToken :   authenticationToken생성완료");
		
		// 생성된 토큰으로 존재하는 사용자인지 검증
		// authentication : 유저가 입력한 username,password등의 인증 정보 담고 있음
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		System.out.println("authentication 만들어졌음.. : "+authentication.getPrincipal().toString());

		// authentication 객체가 session 영역에 저장됨 => 로그인이 되었다는 뜻.
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("로그인 완료됨 : " + principalDetails.getUser().getUserid());
		
		return authentication;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication 진입(인증완료)");
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		System.out.println("successfulAuthentication principalDetails.getUserId() : 내용" + principalDetails.getUsername());

		// HASH 암호방식으로 jwt토큰을 생성한다.
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
//				.withClaim("id", principalDetails.getUser().getUser_id()) // 비공개 claim (내가 넣고 싶은)
				.withClaim("username", principalDetails.getUser().getUserid())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));



		//로그인 응답 패킷 만들기
		//header에 토큰 추가
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);

		System.out.println("successfulAuthentication jwtToken : 내용 : " + jwtToken);
		
		ObjectMapper om = new ObjectMapper();

		LoginDto cmRequestDto = new LoginDto();
		cmRequestDto.setUserid(principalDetails.getUser().getUserid());
		cmRequestDto.setUser_pwd(principalDetails.getUser().getUser_pwd());
		
		String cmRequestDtoJson = om.writeValueAsString(cmRequestDto);
		System.out.println("om.writeValueAsString(cmRequestDto); 내용 : " + cmRequestDtoJson);
		PrintWriter out = response.getWriter();
		out.print(cmRequestDtoJson);
		out.flush();

	}
}