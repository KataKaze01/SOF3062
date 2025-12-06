// src/main/java/com/example/shop/service/OrderService.java
package com.example.shop.service;

import com.example.shop.model.Order;
import com.example.shop.model.User;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Lấy danh sách đơn hàng của người dùng theo email
     */
    public List<Order> getOrdersByUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new ArrayList<>();
        }
        return orderRepository.findByUser(user);
    }

    /**
     * Lấy tất cả đơn hàng (dành cho admin)
     */
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    /**
     * Lấy đơn hàng theo ID
     */
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    /**
     * Lưu đơn hàng
     */
    public void save(Order order) {
        orderRepository.save(order);
    }
}