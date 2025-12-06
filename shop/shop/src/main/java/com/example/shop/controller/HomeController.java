package com.example.shop.controller;

import com.example.shop.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @RequestMapping("/products")
    public String productList() {
        return "product-list";
    }

    @RequestMapping("/product/{id}")
    public String productDetail() {
        return "product-detail";
    }

    @RequestMapping("/cart")
    public String cart() {
        return "cart";
    }

    @RequestMapping("/promotion-products")
    public String promotionProducts() {
        return "promotion-products";
    }

    @RequestMapping("/december-must-read")
    public String decemberMustRead() {
        return "december-must-read";
    }

    @RequestMapping("/sale-lon-mua-le-hoi")
    public String saleLonMuaLeHoi() {
        return "sale-lon-mua-le-hoi";
    }

    @RequestMapping("/giang-sinh-ruc-ro")
    public String giangSinhRucRo() {
        return "giang-sinh-ruc-ro";
    }

    @RequestMapping("/sach-trong-nuoc")
    public String sachTrongNuoc() {
        return "sach-trong-nuoc";
    }

    @RequestMapping("/foreign-books")
    public String foreignBooks() {
        return "foreign-books";
    }

    @RequestMapping("/vpp-duong-cu-hoc-sinh")
    public String vppDuongCuHocSinh() {
        return "vpp-duong-cu-hoc-sinh";
    }

    @RequestMapping("/do-choi")
    public String doChoi() {
        return "do-choi";
    }

    @RequestMapping("/lam-dep-suc-khoe")
    public String lamDepSucKhoe() {
        return "lam-dep-suc-khoe";
    }

    @RequestMapping("/sach-giao-khoa-2025")
    public String sachGiaoKhoa2025() {
        return "sach-giao-khoa-2025";
    }

    @RequestMapping("/vpp-dchs-theo-thuong-hieu")
    public String vppDchsTheoThuongHieu() {
        return "vpp-dchs-theo-thuong-hieu";
    }

    @RequestMapping("/do-choi-theo-thuong-hieu")
    public String doChoiTheoThuongHieu() {
        return "do-choi-theo-thuong-hieu";
    }

    @RequestMapping("/bach-hoa-online-luu-niem")
    public String bachHoaOnlineLuuNiem() {
        return "bach-hoa-online-luu-niem";
    }

    @RequestMapping("/giang-sinh-am-ap")
    public String giangSinhAmAp() {
        return "giang-sinh-am-ap";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("/admin/products")
    public String adminProducts() {
        return "admin-products";
    }

    @RequestMapping("/admin/customers")
    public String adminCustomers() {
        return "admin-customers";
    }

    @RequestMapping("/admin/orders")
    public String adminOrders() {
        return "admin-orders";
    }

    @RequestMapping("/profile")
    public String profile() {
        return "profile";
    }

    @RequestMapping ("/my-orders")
    public String myOrders() {
        return "my-orders";
    }

}