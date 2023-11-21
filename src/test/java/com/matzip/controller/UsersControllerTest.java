package com.matzip.controller;


import com.matzip.dto.UsersFormDto;
import com.matzip.entity.Users;
import com.matzip.service.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;



    @SpringBootTest
    @AutoConfigureMockMvc
    @Transactional
    @TestPropertySource(locations="classpath:application-test.properties")
    class UsersControllerTest {

        @Autowired
        private UsersService usersService;

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        PasswordEncoder passwordEncoder;


//        public Users createUsers(String userid, String user_pwd) throws Exception {
//            UsersFormDto usersFormDto = new UsersFormDto();
//            usersFormDto.setUserid(userid);
//            usersFormDto.setUsername("John Doe");
//            usersFormDto.setUser_address("123 Main Street, City");
//            usersFormDto.setUserphone("123-456-7890");
//            usersFormDto.setUser_pwd(user_pwd);
//            Users users = Users.createUsers(usersFormDto, passwordEncoder);
//            return usersService.saveUsers(users,createMultipartFile());
//
//        }

        //이미지 파일 생성
        MultipartFile createMultipartFile() throws Exception{
            //임시 저장소에, 경로 및 , 파일명 확장자 지정
            String path = "C:/matzip/users/";
            String imageName = "image.jpg";
            MockMultipartFile multipartFile;
            multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[1]);

            return multipartFile;
        }

//        @Test
//        @DisplayName("로그인 성공 테스트")
//        public void loginSuccessTest() throws Exception {
//            String userid = "asd3205";
//            String user_pwd = "1234";
//            this.createUsers(userid, user_pwd);
//            mockMvc.perform(formLogin().userParameter("userid")
//                            .loginProcessingUrl("/users/login")
//                            .user(userid).password(user_pwd))
//                            .andExpect(SecurityMockMvcResultMatchers.authenticated());
//        }

//        @Test
//        @DisplayName("로그인 실패 테스트")
//        public void loginFailTest() throws Exception{
//            String userid = "hong";
//            String password = "1234";
//            this.createUsers(userid, password);
//            mockMvc.perform(formLogin().userParameter("userid")
//                            .loginProcessingUrl("/users/login")
//                            .user(userid).password("12345"))
//                    .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
//        }

    }