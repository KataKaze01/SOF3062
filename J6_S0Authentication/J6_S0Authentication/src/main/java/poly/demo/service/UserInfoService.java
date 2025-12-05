package poly.demo.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
	public String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public Collection<? extends GrantedAuthority> getRoles() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	}
}
