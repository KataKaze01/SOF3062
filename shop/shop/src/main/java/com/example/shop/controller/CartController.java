package com.example.shop.controller;

import com.example.shop.model.CartItem;
import com.example.shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Hiển thị trang giỏ hàng
     */
    @GetMapping
    public String viewCart(Authentication auth, Model model) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        List<CartItem> cartItems = cartService.getCartItems(email);
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            Authentication auth,
            RedirectAttributes redirectAttrs
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        try {
            cartService.addProductToCart(email, productId, quantity);
            redirectAttrs.addFlashAttribute("message", "Sản phẩm đã được thêm vào giỏ hàng!");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/"; // Quay về trang chủ sau khi thêm
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    @PostMapping("/update")
    public String updateCart(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Authentication auth
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        cartService.updateCartItemQuantity(email, productId, quantity);
        return "redirect:/cart";
    }

    /**
     * Xóa sản phẩm khỏi giỏ
     */
    @PostMapping("/remove")
    public String removeFromCart(
            @RequestParam Long productId,
            Authentication auth
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        cartService.removeProductFromCart(email, productId);
        return "redirect:/cart";
    }
}