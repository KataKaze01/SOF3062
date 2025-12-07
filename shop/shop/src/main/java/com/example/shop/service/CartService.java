package com.example.shop.service;

import com.example.shop.model.CartItem;
import com.example.shop.model.Product;
import com.example.shop.model.User;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    public void addProductToCart(String email, Long productId, int quantity) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Người dùng không tồn tại!");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));

        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        Optional<CartItem> existingItemOpt = cartItemRepository.findByUserAndProduct_Id(user, productId);
        if (existingItemOpt.isPresent()) {
            // Nếu có rồi thì cộng dồn số lượng
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            // Nếu chưa có thì tạo mới
            CartItem newItem = new CartItem(user, product, quantity);
            cartItemRepository.save(newItem);
        }
    }

    /**
     * Lấy danh sách giỏ hàng của người dùng
     */
    public List<CartItem> getCartItems(String email) {
        User user = userRepository.findByEmail(email);
        return cartItemRepository.findByUser(user);
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    public void updateCartItemQuantity(String email, Long productId, int newQuantity) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Người dùng không tồn tại!");
        }

        Optional<CartItem> itemOpt = cartItemRepository.findByUserAndProduct_Id(user, productId);
        if (itemOpt.isPresent()) {
            CartItem item = itemOpt.get();
            if (newQuantity <= 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(newQuantity);
                cartItemRepository.save(item);
            }
        }
        // Nếu không tìm thấy, không làm gì cả
    }

    /**
     * Xóa sản phẩm khỏi giỏ
     */
    public void removeProductFromCart(String email, Long productId) {
        User user = userRepository.findByEmail(email);
        cartItemRepository.deleteByUserAndProduct_Id(user, productId);
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    public void clearCart(String email) {
        User user = userRepository.findByEmail(email);
        List<CartItem> items = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(items);
    }
}