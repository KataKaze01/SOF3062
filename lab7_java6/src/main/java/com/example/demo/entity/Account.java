package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;

@Data @Entity @Table(name = "Accounts")
public class Account {
    @Id String username;
    String password;
    String fullname;
    Boolean enabled;
    String role;
}