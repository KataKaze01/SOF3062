package com.poly.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.User;


public interface UserDAO extends JpaRepository<User, String> {}
