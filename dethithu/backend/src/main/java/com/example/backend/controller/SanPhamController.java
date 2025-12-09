package com.example.backend.controller;

import com.example.backend.entity.SanPham;
import com.example.backend.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sanpham")
@CrossOrigin(origins = "http://localhost:5173") // Vite default port
public class SanPhamController {

    @Autowired
    private SanPhamService service;

    @GetMapping
    public ResponseEntity<List<SanPham>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{ma}")
    public ResponseEntity<SanPham> getById(@PathVariable String ma) {
        SanPham sp = service.getById(ma);
        if (sp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sp);
    }

    @PostMapping
    public ResponseEntity<SanPham> create(@RequestBody SanPham sanPham) {
        SanPham saved = service.save(sanPham);
        return ResponseEntity.ok(saved);
    }
}