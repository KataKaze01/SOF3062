package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.poly.DAO.UserDAO;

@Service
public class DaoUserDetailsManager implements UserDetailsService {
    @Autowired
    UserDAO dao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalUser = dao.findById(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        var user = optionalUser.get();
        String password = user.getPassword();

        // Nếu role lưu dạng "ROLE_USER" thì nên cắt bỏ "ROLE_"
        String[] roles = user.getUserRoles().stream()
            .map(ur -> ur.getRole().getId().replace("ROLE_", ""))
            .toList()
            .toArray(String[]::new);

        return User.withUsername(username)
                   .password(password)
                   .roles(roles)
                   .build();
    }
}
