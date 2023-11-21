package com.matzip.service;


import com.matzip.dto.UsersFormDto;
import com.matzip.entity.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class UsersServiceTest {

    @Autowired
    UsersService usersService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Users createUsers() {
        UsersFormDto usersFormDto = new UsersFormDto();
        usersFormDto.setUserid("john_doe");
        usersFormDto.setUsername("John Doe");
        usersFormDto.setUser_pwd("password123");
        usersFormDto.setUser_address("123 Main Street, City");
        usersFormDto.setUserphone("123-456-7890");
        return Users.createUsers(usersFormDto, passwordEncoder);

    }

    //멀티 파트로 이미지 파일 생성
    MultipartFile createMultipartFile() throws Exception{
        //임시 저장소에, 경로 및 , 파일명 확장자 지정
        String path = "C:/matzip/users/";
        String imageName = "image.jpg";
        MockMultipartFile multipartFile;
        multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[1]);

        return multipartFile;
    }

//@Test
//    @DisplayName("회원가입테스트")
//    public void saveUserTest() throws Exception {
//    Users users = createUsers();
//    //users.setUser_image(createMultipartFile());
//    //Users savedUsers = usersService.saveUsers(users,createMultipartFile());
//    System.out.println("users.getUserid(): "+users.getUserid());
//    System.out.println("users.getUser_pwd(): "+users.getUser_pwd());
//    System.out.println("saveUsers(): "+savedUsers.getUserid());
//    assertEquals(users.getUserid(),savedUsers.getUserid());
//    assertEquals(users.getUsername(),savedUsers.getUsername());
//    assertEquals(users.getUser_pwd(),savedUsers.getUser_pwd());
//    assertEquals(users.getUser_address(),savedUsers.getUser_address());
//    assertEquals(users.getUserphone(),savedUsers.getUserphone());
//    assertEquals(users.getUser_role(),savedUsers.getUser_role());
//    assertEquals(users.getUser_image(), savedUsers.getUser_image());
//}

   /* @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateUsersTest() throws Exception {
        Users users1 = createUsers();
        Users users2 = createUsers();
        usersService.saveUsers(users1,createMultipartFile());
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            usersService.saveUsers(users2,createMultipartFile());});
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }*/
}