package com.example.shop.repository;

import com.example.shop.model.CartItem;
import com.example.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    void deleteByUserAndProduct_Id(User user, Long productId);
    CartItem findByUserAndProduct_Id(User user, Long productId);
}