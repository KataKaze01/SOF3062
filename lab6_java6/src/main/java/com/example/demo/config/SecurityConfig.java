package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    // 1. Tạo User trong bộ nhớ (RAM)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder pe) {
        UserDetails user = User.withUsername("user")
                .password(pe.encode("123")).roles("USER").build();
        UserDetails admin = User.withUsername("admin")
                .password(pe.encode("123")).roles("ADMIN").build();
        UserDetails both = User.withUsername("both")
                .password(pe.encode("123")).roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin, both);
    }

    // 2. Cấu hình bộ lọc bảo mật
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) // Tắt CSRF vì dùng Token
                .authorizeHttpRequests(auth -> auth
                		.requestMatchers("/poly/login", "/", "/index.html").permitAll()
                        .requestMatchers("/poly/url1").authenticated()
                        .requestMatchers("/poly/url2").hasRole("USER")
                        .requestMatchers("/poly/url3").hasRole("ADMIN")
                        .requestMatchers("/poly/url4").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated() // Các link khác phải đăng nhập
                )
                // Không dùng Session (STATELESS) vì đã dùng Token
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                // Thêm bộ lọc JWT trước bộ lọc xác thực cơ bản
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService(passwordEncoder()));
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}