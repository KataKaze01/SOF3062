package com.poly.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Product {
    @Id
    String id;

    String name;
    Double price;
    Date date;
    String categoryId;
}
