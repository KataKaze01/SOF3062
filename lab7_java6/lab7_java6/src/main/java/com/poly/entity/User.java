package com.poly.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="users") // tránh conflict với từ khóa SQL: user
public class User {
    @Id
    String username;

    String password;
    String fullname;
    String role;
    Boolean enabled;
}
