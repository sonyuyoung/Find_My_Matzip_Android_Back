package com.matzip.controller;

import com.matzip.dto.*;
import com.matzip.entity.Users;
import com.matzip.repository.UsersRepository;
import com.matzip.service.BoardService;
import com.matzip.service.FollowService;
import com.matzip.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final PasswordEncoder passwordEncoder;
    private final UsersService usersService;
    private final FollowService followService;
    private final BoardService boardService;
    private final UsersRepository usersRepository;


    //회원가입(Rest)
    @PostMapping(value = "/new")
    public void newUsers(@RequestBody UsersFormDto usersFormDto){
        Users users = Users.createUsers(usersFormDto, passwordEncoder);
        usersService.saveUsers(users);
    }


    //전체 유저 목록 조회(Rest)
    @GetMapping("/admin/userList")
    public List<UsersFormDto> findAll(){
        return usersService.findAll();
    }


    //회원 한명 정보 조회(Rest)
    @GetMapping("/aboutUsers/{userid}")
    public UsersFormDto findbyId(@PathVariable String userid) {
        return usersService.findById(userid);
    }

    //Users 업데이트
    @PostMapping(value = "/updateUsers")
    public void updateUsers(@RequestBody UsersFormDto usersFormDto){

        try {
            System.out.println("usersFormDto.getUserid()" + usersFormDto.getUserid());
            System.out.println("usersFormDto.getUsername()" + usersFormDto.getUsername());
            System.out.println("usersFormDto.getUser_address()" + usersFormDto.getUser_address());
            System.out.println("usersFormDto.getUserphone()" + usersFormDto.getUserphone());
            System.out.println("usersFormDto.getUser_image()" + usersFormDto.getUser_image());
            System.out.println("usersFormDto.getGender()" + usersFormDto.getGender());

            usersService.updateUsers(usersFormDto);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping("/delete/{userid}")
    public UsersFormDto deleteById(@PathVariable String userid){
        UsersFormDto users = usersService.findById(userid);
        usersService.deleteById(userid);
        return users;
    }


    //==============================================================================================


    //modUsers폼 호출
    @GetMapping(value = {"/modUsersForm", "/modUsersForm/{pageUserid}"})
    public String modUsersForm(@PathVariable(name = "pageUserid", required = false) String pageUserid, Principal principal, Model model) {

        //내 프로필창에서 수정
        if (pageUserid == null) {
            String userid = principal.getName();
            UsersFormDto usersFormDto = usersService.findById(userid);
            model.addAttribute("usersFormDto", usersFormDto);
        }
        //유저 리스트에서 수정
        else {
            UsersFormDto usersFormDto = usersService.findById(pageUserid);
            model.addAttribute("usersFormDto", usersFormDto);
        }

        return "users/modUsersForm";
    }




//    @GetMapping(value = "/login")
//    public String loginMember() {
//        return "users/usersLoginForm";
//    }
//
//    @GetMapping(value = "/login/error")
//    public String loginError(Model model) {
//        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
//        return "users/usersLoginForm";
//    }

    //내 프로필 조회
    @GetMapping(value = {"/profile", "/profile/{pageUserid}","/profile/{pageUserid}/{page}"})
    public Map<String,Object> myProfileForm(@PathVariable(name = "pageUserid", required = false) String pageUserId, Principal principal, Model model,
                                            BoardSearchDto boardSearchDto, Optional<Integer> page) throws Exception {

        Map<String,Object> map = new HashMap<String,Object>();
        //pageUser == principal         중간저장
           /* pageUserId = principal.getName();
            System.out.println("마이페이지일때 pageUserId: " + pageUserId);*/
//            return "redirect:/users/profile/" + principal.getName();
        //}
//              pageUser == principal
//        pageUserId = principal.getName();

        //myBoardList : 내 게시글 리스트
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainBoardDto> boards = boardService.getBoardPageByUserId(boardSearchDto, pageable, pageUserId);

        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);

        //pageUser의 userDto
        UsersFormDto pageUserDto = usersService.findById(pageUserId);
        System.out.println("다른루트일때 pageUserId: " + pageUserId);

        //현재 로그인된 user의 userDto
        String loginUserId = principal.getName();
        UsersFormDto loginUserDto = usersService.findById(loginUserId);
        System.out.println("loginUserDto : " + loginUserDto);


        //팔로워 리스트
        List<FollowDto> followerDtoList = followService.getFollowerDtoList(pageUserId, principal.getName());
        //팔로잉 리스트
        List<FollowDto> followingDtoList = followService.getFollowingDtoList(pageUserId, principal.getName());

        //로그인 유저가 페이지 유저 팔로우 했는지 여부
        Boolean followcheck = followService.isFollow(pageUserId, principal.getName());

        //pageUser의 팔로잉수, 팔로워수
        int countFromUser = followService.countByFromUser(pageUserId);
        int countToUser = followService.countByToUser(pageUserId);

        //pageUser의 게시글 갯수 (boardService랑 boardController에 코드 추가 필요)
        int countBoard = boardService.countByUserId(pageUserId);
//
//        model.addAttribute("countBoard", countBoard);
//        model.addAttribute("countFromUser", countFromUser);
//        model.addAttribute("countToUser", countToUser);
//        model.addAttribute("followcheck", followcheck);
//        model.addAttribute("followerDtoList", followerDtoList);
//        model.addAttribute("followingDtoList", followingDtoList);
//        model.addAttribute("pageUserDto", pageUserDto);
//        model.addAttribute("loginUserDto", loginUserDto);
        map.put("boards", boards);
        map.put("countBoard", countBoard);
        map.put("countFromUser", countFromUser);
        map.put("countToUser", countToUser);
        map.put("followcheck", followcheck);
        map.put("followerDtoList", followerDtoList);
        map.put("followingDtoList", followingDtoList);
        map.put("pageUserDto", pageUserDto);
        map.put("loginUserDto", loginUserDto);
        return map;
    }



//    @GetMapping("/delete/{userid}")
//    public String deleteById(@PathVariable String userid){
//        usersService.deleteById(userid);
//
//        return "redirect:/users/";
//    }




/*    @DeleteMapping("/deleteFollow/{toUserId}")
    public @ResponseBody ResponseEntity<FollowDto> deleteFollow(@PathVariable String toUserId, Principal principal){
        followService.deleteFollow(toUserId,principal.getName());
        FollowDto followDto = new FollowDto("a","b","c");
        return new ResponseEntity<FollowDto>(followDto, HttpStatus.OK);
    }

    @GetMapping("/insertFollow/{toUserId}")
    *//*@PostMapping("/insertFollow/")*//*
    public @ResponseBody ResponseEntity<FollowDto> insertFollow(@PathVariable String toUserId,Principal principal){
        System.out.println("팔로우 하기전 ");
        followService.insertFollow(toUserId,principal.getName());
        System.out.println("팔로우 하기후 ");
        System.out.println("toUserId 확인: " + toUserId);
        System.out.println("HttpStatus.OK 확인: " + HttpStatus.OK);
        FollowDto followDto = new FollowDto("a","b","c");
        return new ResponseEntity<FollowDto>(followDto, HttpStatus.OK);

    }*/

    @DeleteMapping("/deleteFollow/{toUserId}")
//    public @ResponseBody ResponseEntity<Map<String, Object>> deleteFollow(@PathVariable String toUserId, Principal principal) {
    public  void deleteFollow(@PathVariable String toUserId, Principal principal) {
        System.out.println("toUserId : " + toUserId);
        System.out.println("principal.getName() : " + principal.getName());
        followService.deleteFollow(toUserId, principal.getName());
        Map<String, Object> result = new HashMap<>();
        result.put("data", toUserId);
       // return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }

    @GetMapping("/insertFollow/{toUserId}")
    public void insertFollow(@PathVariable String toUserId, Principal principal) {
        followService.insertFollow(toUserId, principal.getName());

    }

    //맛잘알게시판
    @GetMapping(value = {"/matjalal"})
    public ResponseEntity<List<MainBoardDto>> getMatjalalBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            BoardSearchDto boardSearchDto,
            Principal principal) throws Exception {

        //로그인 유저의 following 리스트
        List<String> toUserIdList = followService.getFollowingIdList(principal.getName());

        //myBoardList : 내 게시글 리스트
        Pageable pageable = PageRequest.of(page, size);
        Page<MainBoardDto> boards = boardService.getBoardPageByFollowList(boardSearchDto, pageable, toUserIdList);

        return ResponseEntity.ok(boards.getContent());
    }

    //231218 김경태 작업중 새 맛잘알 리스트
    @GetMapping(value = {"/newmatjalal"})
    public ResponseEntity<List<NewMainBoardDto>> getNewMatjalalBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            BoardSearchDto boardSearchDto,
            Principal principal) throws Exception {

        //로그인 유저의 following 리스트
        List<String> toUserIdList = followService.getFollowingIdList(principal.getName());

        //myBoardList : 내 게시글 리스트
        Pageable pageable = PageRequest.of(page, size);
        Page<NewMainBoardDto> boards = boardService.getNewBoardPageByFollowList(boardSearchDto, pageable, toUserIdList);

        return ResponseEntity.ok(boards.getContent());
    }
    //231218 김경태 작업중 새 맛잘알 리스트





    //유저 리스트(page)
    @GetMapping("/getAllUsers/{text}")
    public ResponseEntity<List<UsersFormDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @PathVariable String text) {

        Pageable pageable = PageRequest.of(page, size);

        Page<UsersFormDto> users = usersRepository.findByUseridContaining(text, pageable);
        return ResponseEntity.ok(users.getContent());
    }

}