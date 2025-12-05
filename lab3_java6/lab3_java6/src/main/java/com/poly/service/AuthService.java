package com.poly.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("auth")
public class AuthService {
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public UserDetails getUser() {
		return (UserDetails) this.getAuthentication().getPrincipal();
	}
	
	public Object getUsername() {
		return this.getAuthentication().getName();
	}
	
	public List<String> getRoles(){
		return this.getAuthentication().getAuthorities().stream().map(au -> au.getAuthority().substring(5)).toList();
	}
	
	// ĐÃ SỬA LỖI CHÍNH TẢ TẠI ĐÂY: iaAuthenticated -> isAuthenticated
	public boolean isAuthenticated() {
		Object username = this.getUsername();
		return (username !=null && !username.equals("anonymousUser"));
	}
	
	public boolean haveAnyRole(String... rolesToCheck) {
		var grantedRoles = this.getRoles();
		return Stream.of(rolesToCheck).anyMatch(role -> grantedRoles.contains(role));
	}
	
	public void authenticate (String username, String password, String... roles) {
		UserDetails user = User.withUsername (username).password (password).roles (roles).build();
		Authentication authentication =
		new UsernamePasswordAuthenticationToken (user, user.getPassword(), user.getAuthorities()); SecurityContextHolder.getContext().setAuthentication (authentication);
	}
}