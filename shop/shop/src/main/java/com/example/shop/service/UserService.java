package com.example.shop.service;

import com.example.shop.model.Role;
import com.example.shop.model.User;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Đăng ký người dùng mới
     * - Mã hóa mật khẩu (nếu bạn dùng BCrypt)
     * - Gán vai trò mặc định là ROLE_USER
     */
    public void registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // Gán vai trò mặc định là USER
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role USER không tồn tại!"));
        user.setRoles(Collections.singleton(userRole));

        // Lưu người dùng vào database
        userRepository.save(user);
    }

    /**
     * Cập nhật thông tin người dùng
     */
    public void updateUser(User user, String email) {
        User existing = userRepository.findByEmail(email);
        if (existing == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        // Cập nhật các trường
        existing.setName(user.getName());
        existing.setPhone(user.getPhone());
        existing.setAddress(user.getAddress());

        // Nếu người dùng nhập mật khẩu mới
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // existing.setPassword(passwordEncoder.encode(user.getPassword())); // Bỏ comment nếu dùng BCrypt
        }

        userRepository.save(existing);
    }

    /**
     * Lấy thông tin người dùng theo email
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Lấy tất cả người dùng (dành cho admin)
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}