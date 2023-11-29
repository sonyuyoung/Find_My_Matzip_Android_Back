package com.matzip.config;

import com.matzip.config.jwt.JwtAuthenticationFilter;
import com.matzip.config.jwt.JwtAuthorizationFilter;
import com.matzip.config.oauth.PrincipalOauth2UserService;
import com.matzip.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//해당 클래스를 Configuration으로 등록한다.
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UsersService usersService;

//    @Autowired
//    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //jwt 토큰방식 사용!
        http
                .csrf().disable() //csrf 보안 설정 비활성화 (사용하기 위해서는 앱에서 csrf토큰 값 보내줘야됨)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증 할 것이므로 세션 사용 비활성화
                .and()

                .httpBasic().disable() //사용자 인증방법으로는 HTTP Basic Authentication을 사용 안한다.
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))  // 로그인 -> jwt를 사용해서 인증 처리
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), usersService)) // 사용자 확인 -> jwt를 사용해서 인가 처리 (Principal 생성)

                .authorizeRequests() //인가 처리(권한 확인)
//                .antMatchers("/users/admin/**","/boards/admin/**")
//                .access("hasRole('ADMIN')") //ADMIN만 접근 허용
//                .antMatchers("/login","/users/new").permitAll()
//                .anyRequest().access("hasRole('USER')or hasRole('ADMIN')");
                .anyRequest().permitAll(); //위의 권한처리 안먹혀서 수정필요 (일단 모두 허용)


        //1차 프로젝트 권한관리 코드
       /* http.authorizeRequests()
                .mvcMatchers("/","/admin/**", "/users/**","/item/**", "/images/**", "/map","/restaurant/main").permitAll()
                .mvcMatchers("/users/userspage/","/admin/boards").hasRole("ADMIN")
                .anyRequest().authenticated()
        */

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/users/new");
    }

}