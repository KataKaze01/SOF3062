package com.example.shop.controller;

import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        try {
            userService.forgotPassword(email);
            model.addAttribute("success", "Chức năng không gửi email trong phiên bản này.\nVui lòng kiểm tra token trong database và truy cập:\nhttp://localhost:8080/reset-password?token=[TOKEN]");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model
    ) {
        String result = userService.resetPassword(token, password, confirmPassword);
        if ("success".equals(result)) {
            model.addAttribute("success", "Mật khẩu đã được cập nhật thành công!");
        } else {
            model.addAttribute("error", result);
        }
        model.addAttribute("token", token);
        return "reset-password";
    }
}