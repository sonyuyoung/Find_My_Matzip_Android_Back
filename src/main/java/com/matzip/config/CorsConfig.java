package com.matzip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

//	Cross Origin에서 자원을 요청하기 위해서는 다음과 같은 과정을 거친다.
//
//	HTTP 통신 헤더인 Origin 헤더에 요청을 보내는 곳의 정보를 담고 서버로 요청을 보낸다.
//	이후 서버는 Access-Control-Allow-Origin헤더에 허용된 Origin이라는 정보를 담아 보낸다
//	클라이언트는 헤더의 값과 비교해 정상 응답임을 확인하고 지정된 요청을 보낸다.
//	서버는 요청을 수행하고 200 코드를 응답한다.
	
//	cors관련 설정을 포함한 필터.
//
//	기본적으로 서버 또는 지정된 특정 도메인의 요청만 허용하지만 프런트가 정해져있지 않기 때문에 모든 도메인을 허용하는 방식으로 설정.
//

//
//	● addAllowedOriginPattern : 허용할 도메인 목록
//

//

//

	
	//Class CorsFilter 공식 문서
	//https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/CorsFilter.html
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		
//		● setAllowCredentials : 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할수 있게 할지를 설정
		config.setAllowCredentials(true);
		
		
		config.addAllowedOrigin("*");
		
//		● addAllowedHeader : 허용할 헤더 목록
		config.addAllowedHeader("*");
		
//		● addAllowedMethod : 허용할 메서드(GET, PUT, 등) 목록
		config.addAllowedMethod("*");
		
//		● source.registerCorsConfiguration : 지정한 url에 config 적용
		source.registerCorsConfiguration("/**", config);
		
		return new CorsFilter(source);
	}
}