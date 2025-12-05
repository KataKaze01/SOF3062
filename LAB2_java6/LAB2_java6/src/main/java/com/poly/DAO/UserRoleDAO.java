package com.poly.DAO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.UserRole;
public interface UserRoleDAO extends JpaRepository<UserRole, Long> {}
