package com.example.demo.controller;

import com.example.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

class AuthRequest {
    public String username;
    public String password;
    // getters setters (hoặc để public cho nhanh gọn trong lab)
}

@CrossOrigin("*")
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/poly/login")
    public Map<String, String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        // 1. Xác thực username/password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        );

        // 2. Nếu đúng, tạo Token và trả về
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.username);
            return Map.of("token", token);
        } else {
            throw new UsernameNotFoundException("Tài khoản hoặc mật khẩu sai!");
        }
    }
}