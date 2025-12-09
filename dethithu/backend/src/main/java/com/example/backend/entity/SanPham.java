package com.example.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "SanPham")
public class SanPham {

    @Id
    @Column(name = "maSanPham")
    private String maSanPham;

    @Column(name = "tenSanPham", nullable = false)
    private String tenSanPham;

    @Column(name = "donGia", nullable = false)
    private BigDecimal donGia;

    @Column(name = "hanSuDung", nullable = false)
    private LocalDate hanSuDung;

    @Column(name = "hinhAnh")
    private String hinhAnh;

    @Column(name = "nguonGoc", nullable = false)
    private String nguonGoc;

    // Constructors
    public SanPham() {}

    public SanPham(String maSanPham, String tenSanPham, BigDecimal donGia, LocalDate hanSuDung, String hinhAnh, String nguonGoc) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.donGia = donGia;
        this.hanSuDung = hanSuDung;
        this.hinhAnh = hinhAnh;
        this.nguonGoc = nguonGoc;
    }

    // Getters and Setters
    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public BigDecimal getDonGia() { return donGia; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }

    public LocalDate getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(LocalDate hanSuDung) { this.hanSuDung = hanSuDung; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getNguonGoc() { return nguonGoc; }
    public void setNguonGoc(String nguonGoc) { this.nguonGoc = nguonGoc; }
}