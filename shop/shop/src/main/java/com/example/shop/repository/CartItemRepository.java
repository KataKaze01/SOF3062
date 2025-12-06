package com.example.shop.repository;

import com.example.shop.model.CartItem;
import com.example.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);

    // ✅ THÊM DÒNG NÀY:
    Optional<CartItem> findByUserAndProduct_Id(User user, Long productId);

    // ✅ THÊM DÒNG NÀY ĐỂ DÙNG TRONG SERVICE:
    void deleteByUserAndProduct_Id(User user, Long productId);
}