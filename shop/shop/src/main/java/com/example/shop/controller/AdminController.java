package com.example.shop.controller;

import com.example.shop.model.Order;
import com.example.shop.model.Product;
import com.example.shop.model.User;
import com.example.shop.service.OrderService;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalProducts", productService.getAll().size());
        model.addAttribute("totalCustomers", userService.getAllUsers().size());
        model.addAttribute("totalOrders", orderService.getAllOrders().size());
        return "admin";
    }

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

    @PostMapping("/customers/add")
    public String addCustomer(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam Long roleId,
            RedirectAttributes redirectAttrs
    ) {
        try {
            userService.createCustomer(name, email, password, phone, address, roleId);
            redirectAttrs.addFlashAttribute("message", "Thêm khách hàng thành công!");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/customers";
    }

    @PostMapping("/customers/update")
    public String updateCustomer(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam Long roleId,
            RedirectAttributes redirectAttrs
    ) {
        try {
            userService.updateCustomer(id, name, phone, address, roleId);
            redirectAttrs.addFlashAttribute("message", "Cập nhật khách hàng thành công!");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            userService.deleteCustomer(id);
            redirectAttrs.addFlashAttribute("message", "Xóa khách hàng thành công!");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/customers";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    // Thêm sản phẩm
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    // Cập nhật sản phẩm
    @PostMapping("/products/update")
    public String updateProduct(@ModelAttribute Product product) {
        productService.update(product);
        return "redirect:/admin/products";
    }

    // Xóa sản phẩm
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }
}