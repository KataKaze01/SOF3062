package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;

@Data @Entity @Table(name = "Categories")
public class Category implements Serializable {
    @Id String id;
    String name;
    @JsonIgnore @OneToMany(mappedBy = "category") List<Product> products;
}