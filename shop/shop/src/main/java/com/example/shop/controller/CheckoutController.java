package com.example.shop.controller;

import com.example.shop.service.CartService;
import com.example.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String checkout(Authentication auth, Model model) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        model.addAttribute("cartItems", cartService.getCartItems(email));
        return "checkout";
    }

    @PostMapping("/checkout/process")
    public String processCheckout(Authentication auth, RedirectAttributes redirectAttrs) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        try {
            orderService.createOrderFromCart(email);
            redirectAttrs.addFlashAttribute("message", "Đặt hàng thành công!");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/";
    }
}