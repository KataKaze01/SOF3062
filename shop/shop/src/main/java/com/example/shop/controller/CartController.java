package com.example.shop.controller;

import com.example.shop.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Hiển thị giỏ hàng
     */
    @GetMapping
    public String viewCart(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("cartItems", cartService.getCartItems(email));
        return "cart";
    }

    /**
     * Thêm sản phẩm vào giỏ
     */
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            Authentication auth,
            RedirectAttributes redirectAttrs,
            HttpServletRequest request // ← THÊM DÒNG NÀY
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

        // Quay lại trang trước đó
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    @PostMapping("/update")
    public String updateCartItem(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Authentication auth
    ) {
        String email = auth.getName();
        cartService.updateCartItemQuantity(email, productId, quantity);
        return "redirect:/cart";
    }

    /**
     * Xóa sản phẩm khỏi giỏ
     */
    @PostMapping("/remove")
    public String removeCartItem(
            @RequestParam Long productId,
            Authentication auth
    ) {
        String email = auth.getName();
        cartService.removeProductFromCart(email, productId);
        return "redirect:/cart";
    }
}