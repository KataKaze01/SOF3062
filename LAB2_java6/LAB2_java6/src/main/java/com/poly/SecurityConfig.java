package com.poly;

import org.springframework.security.core.userdetails.User; // Class User chính xác

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.poly.service.DaoUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired DaoUserDetailsManager daoManager;
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
//	@Bean
//	public UserDetailsService userDetailsService(PasswordEncoder pe) {
//		String password = pe.encode("123");
//		UserDetails user1 = User.withUsername("user@gmail.com").password(password).roles("USER").build();
//		UserDetails user2 = User.withUsername("admin@gmail.com").password(password).roles("ADMIN").build();
//		UserDetails user3 = User.withUsername("both@gmail.com").password(password).roles("USER", "ADMIN").build();
//		return new InMemoryUserDetailsManager(user1, user2, user3);
//	}
	@Bean
	public UserDetailsService userDetailsService() {
		return new DaoUserDetailsManager();
	}
	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
		// Bỏ cầu hình mặc định CRSF và CORS
		http.csrf(csrf -> csrf.disable()).cors (cors -> cors.disable());
		// Phân quyền sử dụng
		http.authorizeHttpRequests (req -> {
			req.requestMatchers("/poly/urll").authenticated();
			req.requestMatchers("/poly/url2").hasAnyRole("USER");
			req.requestMatchers("/poly/url3").hasAnyRole("ADMIN");
			req.requestMatchers("/poly/url4").hasAnyRole ("ADMIN", "USER");
			req.anyRequest().permitAll();
		});
		// Từ chối truy xuất nếu vai trò ko phủ hợp
		http.exceptionHandling (denied -> denied.accessDeniedPage("/unauthorized.html"));
		// Form đăng nhập mặc định login
		http.formLogin (login-> login.permitAll());
		return http.build();
	}
	
}
