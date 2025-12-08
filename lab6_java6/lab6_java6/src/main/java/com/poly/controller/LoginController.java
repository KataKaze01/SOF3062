package com.poly.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.poly.service.JwtService;

@RestController
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @PostMapping("/poly/login")
    public Object login(@RequestBody Map<String, String> userInfo) {

        String username = userInfo.get("username");
        String password = userInfo.get("password");

        var authInfo = new UsernamePasswordAuthenticationToken(username, password);
        var authentication = authenticationManager.authenticate(authInfo);

        if (authentication.isAuthenticated()) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtService.create(user, 20 * 60); // 20 phút
            return Map.of("token", token);
        }

        throw new UsernameNotFoundException("Username not found!");
    }
}
