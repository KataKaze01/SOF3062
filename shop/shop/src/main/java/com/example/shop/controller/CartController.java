package com.example.shop.controller;

import com.example.shop.service.CartService;
import com.example.shop.service.OrderService;
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
     * Hiển thị trang giỏ hàng
     */
    @GetMapping
    public String viewCart(Authentication auth, Model model) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        model.addAttribute("cartItems", cartService.getCartItems(email));
        return "cart";
    }

    /**
     * Thêm sản phẩm vào giỏ
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

        return "redirect:/"; // Quay về trang chủ
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
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        cartService.updateCartItemQuantity(email, productId, quantity);
        return "redirect:/cart";
    }

    @Autowired
    private OrderService orderService;


    @PostMapping("/checkout")
    public String checkout(Authentication auth, RedirectAttributes redirectAttrs) {
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
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        cartService.removeProductFromCart(email, productId);
        return "redirect:/cart";
    }
}