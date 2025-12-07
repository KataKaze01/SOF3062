package com.example.shop.service;

import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // ← BẮT BUỘC
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Lấy danh sách sản phẩm nổi bật
     */
    public List<Product> getFeaturedProducts() {
        return productRepository.findAll(); // hoặc bạn có thể lọc theo điều kiện
    }

    /**
     * Lấy tất cả sản phẩm
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Lấy sản phẩm theo ID
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Lưu sản phẩm mới
     */
    public void save(Product product) {
        productRepository.save(product);
    }


    /**
     * Lấy tất cả sản phẩm
     */
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    /**
     * Xóa sản phẩm theo ID
     */
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}