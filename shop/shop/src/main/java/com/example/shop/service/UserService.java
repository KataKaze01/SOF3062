package com.example.shop.service;

import com.example.shop.model.PasswordResetToken;
import com.example.shop.model.Role;
import com.example.shop.model.User;
import com.example.shop.repository.PasswordResetTokenRepository;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    /**
     * Xử lý quên mật khẩu
     */
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email không tồn tại!");
        }

        // Xóa token cũ nếu có
        if (user.getResetToken() != null) {
            // Tìm và xóa token cũ trong bảng password_reset_tokens
            PasswordResetToken oldToken = tokenRepository.findByToken(user.getResetToken()).orElse(null);
            if (oldToken != null) {
                tokenRepository.delete(oldToken);
            }
        }

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        // Tạo token entity và lưu vào bảng
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);
    }

    /**
     * Đặt lại mật khẩu từ token
     */
    public String resetPassword(String token, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            return "Mật khẩu xác nhận không khớp!";
        }

        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
        if (resetToken == null) {
            return "Token không hợp lệ!";
        }
        if (resetToken.isExpired()) {
            return "Token đã hết hạn!";
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword)); // Mã hóa mật khẩu
        user.setResetToken(null); // Xóa token sau khi dùng
        userRepository.save(user);

        // Xóa token khỏi bảng
        tokenRepository.delete(resetToken);

        return "success";
    }
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
     * Đổi mật khẩu người dùng
     */
    public String changePassword(String email, String currentPassword, String newPassword, String confirmNewPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "Người dùng không tồn tại!";
        }

        // Kiểm tra mật khẩu hiện tại có đúng không
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "Mật khẩu hiện tại không đúng!";
        }

        // Kiểm tra mật khẩu mới có trùng với xác nhận không
        if (!newPassword.equals(confirmNewPassword)) {
            return "Mật khẩu xác nhận không khớp!";
        }

        // Mã hóa mật khẩu mới và cập nhật vào database
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "success";
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