package com.example.shop.service;

import com.example.shop.model.CartItem;
import com.example.shop.model.Order;
import com.example.shop.model.OrderItem;
import com.example.shop.model.Product;
import com.example.shop.model.User;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    /**
     * Tạo đơn hàng từ giỏ hàng
     */
    public void createOrderFromCart(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Người dùng không tồn tại!");
        }

        List<CartItem> cartItems = cartService.getCartItems(email);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống!");
        }

        // Tạo đơn hàng mới
        Order order = new Order(user);
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());

            // Đảm bảo unitPrice không null
            BigDecimal unitPrice = cartItem.getUnitPrice();
            if (unitPrice == null) {
                unitPrice = cartItem.getProduct().getPrice(); // Lấy từ sản phẩm
            }
            orderItem.setUnitPrice(unitPrice);

            order.getOrderItems().add(orderItem);
            total = total.add(orderItem.getSubtotal());
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        // Xóa giỏ hàng sau khi tạo đơn
        cartService.clearCart(email);
    }

    /**
     * Lấy tất cả đơn hàng của người dùng
     */
    public List<Order> getOrdersByUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return List.of();
        }
        return orderRepository.findByUser(user);
    }

    /**
     * Lấy tất cả đơn hàng (dành cho admin)
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}