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
     * Giảm số lượng tồn kho sau khi đặt hàng
     */
    @Transactional
    public void decreaseStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Số lượng tồn kho không đủ!");
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    /**
     * Cập nhật số lượng tồn kho sản phẩm (dành cho admin)
     */
    public void updateStock(Long productId, Integer newStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));

        product.setStock(newStock);
        productRepository.save(product);
    }
    /**
     * Lấy tất cả sản phẩm
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Cập nhật sản phẩm
     */
    public void update(Product product) {
        // Kiểm tra xem sản phẩm có tồn tại không
        if (productRepository.existsById(product.getId())) {
            productRepository.save(product);
        } else {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        }
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