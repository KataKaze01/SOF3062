package com.poly;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import org.springframework.security.core.Authentication;


import com.poly.service.AuthService;

import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	AuthService authService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PasswordEncoder pe) throws Exception {
        // Cấu hình mặc định CSRF và CORS
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable());

        // Phân quyền sử dụng
        http.authorizeHttpRequests(req -> {
            req.requestMatchers("/poly/url1").authenticated();
            req.requestMatchers("/poly/url2").hasAnyRole("USER");
            req.requestMatchers("/poly/url3").hasAnyRole("ADMIN");
            req.requestMatchers("/poly/url4").hasAnyRole("ADMIN", "USER");
            req.anyRequest().permitAll();
        });

        // Từ chối truy xuất nếu vai trò không phù hợp
        http.exceptionHandling(denied -> denied.accessDeniedPage("/unauthorized.html"));

        // Form đăng nhập mặc định /login
        http.formLogin(login -> login.permitAll());

        http.oauth2Login(login -> {
            login.permitAll();
            login.successHandler((request, response, authentication) -> {
                // Đọc UserDetails chứa thông tin người dùng từ Google
                DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
                String username = user.getName(); // Hoặc user.getAttribute("email")
                String password = "{noop}";
                String role = "OAUTH";

                // Tạo đối tượng Authentication mới và thay đổi đối tượng Authentication hiện tại
                UserDetails newUser = org.springframework.security.core.userdetails.User
                        .withUsername(username)
                        .password(password)
                        .roles(role)
                        .build();

                Authentication newauth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        newUser,
                        null,
                        newUser.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(newauth);

                // Chuyển về trang yêu cầu bảo mật hoặc trang chủ
                HttpSession session = request.getSession();
                String attr = "SPRING_SECURITY_SAVED_REQUEST";
                DefaultSavedRequest savedRequest = (DefaultSavedRequest) session.getAttribute(attr);
                
                String redirectUrl = (savedRequest == null) ? "/" : savedRequest.getRedirectUrl();
                
                response.sendRedirect(redirectUrl);
            });
        });

        return http.build();
    }
	
}
