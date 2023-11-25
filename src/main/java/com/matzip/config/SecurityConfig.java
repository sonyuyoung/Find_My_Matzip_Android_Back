package com.matzip.config;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UsersService usersService;
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 수업 때, 웹 앞단, 히든 ,  csrf 토큰 값을 항상 넣어서 서버에 전달.
        // 테스트 라서,  csrf 설정임시로 끄고,
        // 만약, csrf 작업 하고 싶다. 앱, 값 숨겨서 같이 전달해야함.
        // csrf 사용하거나
        //  jwt 사용하거나,
        // sns  인증.
        http.csrf().disable()
                .formLogin().disable();
//        http.formLogin()
//                .loginPage("/users/login") //사용자 정의 로그인 페이지
//                .defaultSuccessUrl("/") // 로그인 성공 후 이동 페이지
//                .usernameParameter("userid")// 아이디 파라미터명 설정
//                .passwordParameter("user_pwd")
//                .failureUrl("/users/login/error") // 로그인 실패 후 이동 페이지
//
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout")) //사용자 정의 로그아웃 페이지
//                .logoutSuccessUrl("/") // 로그아웃 후 이동 페이지



//                .and()
//                .oauth2Login()
//                .loginPage("/users/login") // OAuth2 로그인 페이지
//                .userInfoEndpoint()
//                .userService(principalOauth2UserService);
//
        ;

        http.authorizeRequests()
                .mvcMatchers("/**").permitAll()
                .anyRequest().authenticated()
        ;

       /* http.authorizeRequests()
                .mvcMatchers("/","/admin/**", "/users/**","/item/**", "/images/**", "/map","/restaurant/main").permitAll()
                .mvcMatchers("/users/userspage/","/admin/boards").hasRole("ADMIN")
                .anyRequest().authenticated()
        ;*/

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;
    }



}