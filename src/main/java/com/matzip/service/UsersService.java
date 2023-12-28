package com.matzip.service;


import com.matzip.dto.LoginDto;
import com.matzip.dto.UsersFormDto;
import com.matzip.entity.Users;
import com.matzip.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class UsersService {

    @Value("${userImgLocation}")
    private String userImgLocation;
    private final UsersRepository usersRepository;
    private final FileService fileService;


//    public Users saveUsers(Users users, MultipartFile userImgFile) throws Exception {
//        String oriImgName = userImgFile.getOriginalFilename();
//        String imgName = "";
//        String imgUrl = "";
//
//        //파일 업로드
//        if (!StringUtils.isEmpty(oriImgName)) {
//            imgName = fileService.uploadFile(userImgLocation, oriImgName, userImgFile.getBytes());
//            imgUrl = "/images/users/" + imgName;
//        }
//
//        //상품 이미지 정보 저장
//        users.setUser_image(imgUrl);
//
//        validateDuplicateUsers(users);
//        return usersRepository.save(users);
//    }

    public void saveUsers(Users users){
        validateDuplicateUsers(users);
        usersRepository.save(users);
    }

    //로그인시 확인
    public boolean vertifyLogin(LoginDto loginDto,PasswordEncoder passwordEncoder){
        Users loginUser = usersRepository.findByUserid(loginDto.getUserid());

        //password 검증결과 return
        return loginUser.checkPassword(loginDto.getUser_pwd(),passwordEncoder);
    }

    //회원정보 변경(Rest)
    public void updateUsers(UsersFormDto usersFormDto){
        //객체 찾기
        Users users = usersRepository.findByUserid(usersFormDto.getUserid());

        //usersFormDto(수정폼 입력 정보)로 data변경
        users.updateUsers(usersFormDto);
        usersRepository.save(users);
    }


    private void validateDuplicateUsers(Users users) {
        Users findUsers = usersRepository.findByUserid(users.getUserid());
        if (findUsers != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }




    public List<UsersFormDto> findAll() {
        //엔티티리스트 객체를 dto리스트객체로 컨트롤러에게 줘야함. 변환필요 .
        List<Users> usersEntityList = usersRepository.findAll();
        List<UsersFormDto> usersFormDtoList = new ArrayList<>();
        //엔티티가 여러개 담긴 리스트를 dto여러개인 리스트로 .
        //usersEntityList 를 하나씩 usersFormDtoList 로 옮겨줘야함
        for (Users users : usersEntityList) {
            usersFormDtoList.add(UsersFormDto.toUsersDto(users));
        }
        return usersFormDtoList;
    }

    public UsersFormDto findById(String userid) {
        Optional<Users> optionalUsers = usersRepository.findById(userid);
        return optionalUsers.map(UsersFormDto::toUsersDto).orElse(null);
    }

    //토큰 생성시 사용하는 로직(userid로 Users객체 가져오기)
    public Users findUsersById(String userid) {
        return usersRepository.findByUserid(userid);
    }

    public void deleteById(String userid) {
        usersRepository.deleteById(userid);
    }

    public void updateUserInfo(UsersFormDto usersFormDto) {
        // 여기에서 사용자 정보 업데이트 작업 수행
        // usersFormDto를 엔티티로 변환하여 업데이트할 수 있어야 함
        Users users = Users.aboutUsers(usersFormDto);
        usersRepository.save(users);
    }

    public Users findByUserId(String userid) {
        return usersRepository.findByUserid(userid);
    }

}