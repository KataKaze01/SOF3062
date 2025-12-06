package com.example.shop.controller;

import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.getAll());
        return "admin/products";
    }

    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/customers";
    }
}
