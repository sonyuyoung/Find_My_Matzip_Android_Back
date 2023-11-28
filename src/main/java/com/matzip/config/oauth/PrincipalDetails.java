package com.matzip.config.oauth;

import com.matzip.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PrincipalDetails implements UserDetails {

private Users users;
	public PrincipalDetails(Users users ) {
		this.users = users;
	}
	
	public Users getUser() {
		return users;
	}
	

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return users.getUser_pwd();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		System.out.println("===================getUsername 호출 =========principalDetail");
		return users.getUserid();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

}
