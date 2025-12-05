package poly.demo.service;

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
	/**
	 * Lấy đối tượng xác thực
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	/**
	 * Lấy User đăng nhập
	 */
	public UserDetails getUser() {
		return (UserDetails) this.getAuthentication().getPrincipal();
	}
	/**
	 * Lấy tên đăng nhập
	 */
	public String getUsername() {
		return this.getAuthentication().getName();
	}
	/**
	 * Lấy các vai trò của user đăng nhập
	 */
	public List<String> getRoles() {
		return this.getAuthentication().getAuthorities().stream()
				.map(au -> au.getAuthority().substring(5)).toList();
	}
	/**
	 * Kiểm tra đăng nhập hay chưa
	 */
	public boolean isAuthenticated() {
		String username = this.getUsername();
		return (username != null && !username.equals("anonymousUser"));
	}
	/**
	 * Kiểm tra vai trò của user đăng nhập
	 * @param rolesToCheck các vai trò cần kiểm tra
	 * @return true nếu user đăng nhập có ít nhất một trong các vai trò cần kiểm tra
	 */
	public boolean hasAnyRole(String... rolesToCheck) {
		var grantedRoles = this.getRoles();
		return Stream.of(rolesToCheck).anyMatch(role -> grantedRoles.contains(role));
	}
	/**
	 * Thay đổi đối tượng xác thực với thông tin user mới
	 * @param username tên đăng nhập của user mới
	 * @param password mật khẩu đăng nhập của user mới
	 * @param roles vai trò của user mới
	 */
	public void authenticate(String username, String password, String... roles) {
		UserDetails user = User.withUsername(username).password(password).roles(roles).build();
		Authentication authentication = 
				new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}	
}