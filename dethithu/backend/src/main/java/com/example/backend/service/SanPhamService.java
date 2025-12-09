package com.example.backend.service;

import com.example.backend.entity.SanPham;
import com.example.backend.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository repository;

    public List<SanPham> getAll() {
        return repository.findAll();
    }

    public SanPham getById(String ma) {
        Optional<SanPham> sp = repository.findById(ma);
        return sp.orElse(null);
    }

    public SanPham save(SanPham sanPham) {
        return repository.save(sanPham);
    }
}