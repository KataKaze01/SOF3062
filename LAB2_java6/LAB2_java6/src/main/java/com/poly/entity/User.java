package com.poly.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "J6Users")
public class User {
    @Id
    String username;   // ✅ Sửa sai chính tả: "usename" → "username"

    String password;
    boolean enabled;   // ✅ Sửa: "enable" → "enabled" (theo chuẩn Spring Security)

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER) // ✅ mappedBy = tên biến trong UserRole
    List<UserRole> userRoles;
}
