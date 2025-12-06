package com.example.shop.controller;

import com.example.shop.model.Order;
import com.example.shop.model.User;
import com.example.shop.service.OrderService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    /**
     * Trang hồ sơ cá nhân
     */
    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        String email = auth.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("user", user); // ← Đảm bảo dòng này có
        return "profile";
    }

    /**
     * Cập nhật hồ sơ cá nhân
     */
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user, Authentication auth) {
        String email = auth.getName();
        userService.updateUser(user, email);
        return "redirect:/profile";
    }

    /**
     * Trang đơn hàng cá nhân
     */
    @GetMapping("/my-orders")
    public String myOrders(Authentication auth, Model model) {
        String email = auth.getName();
        List<Order> orders = orderService.getOrdersByUser(email);
        model.addAttribute("orders", orders);
        return "my-orders";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword,
            Authentication auth,
            RedirectAttributes redirectAttrs
    ) {
        String email = auth.getName();
        String result = userService.changePassword(email, currentPassword, newPassword, confirmNewPassword);

        if ("success".equals(result)) {
            redirectAttrs.addFlashAttribute("message", "Đổi mật khẩu thành công!");
        } else {
            redirectAttrs.addFlashAttribute("error", result);
        }

        return "redirect:/profile";
    }
}