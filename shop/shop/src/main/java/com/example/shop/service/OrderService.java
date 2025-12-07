package com.example.shop.service;

import com.example.shop.model.Order;
import com.example.shop.model.OrderItem;
import com.example.shop.model.User;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;



    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void saveOrder(Order order) {
        // Lưu đơn hàng
        Order savedOrder = orderRepository.save(order);

        // Với mỗi sản phẩm trong giỏ hàng → giảm số lượng tồn kho
        for (OrderItem item : order.getOrderItems()) {
            productService.decreaseStock(item.getProduct().getId(), item.getQuantity());
        }
    }
    public List<Order> getOrdersByUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return List.of();
        }
        return orderRepository.findByUser(user);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void save(Order order) {
        orderRepository.save(order);
    }
}