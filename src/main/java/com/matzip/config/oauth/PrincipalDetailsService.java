package com.matzip.config.oauth;

import com.matzip.entity.Users;
import com.matzip.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	private final UsersService usersService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println("PrincipalDetailsService: 진입");
		
		// username 디비에서 불러오기.
		Users user = usersService.findUsersById(username);
		// PrincipalDetails 객체 에 해당 유저 넣기.
		return new PrincipalDetails(user);
	}

}
