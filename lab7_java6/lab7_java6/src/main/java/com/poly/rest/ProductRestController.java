package com.poly.rest;

import com.poly.dao.ProductDAO;
import com.poly.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    ProductDAO dao;

    @GetMapping
    public List<Product> getAll() {
        return dao.findAll();
    }

    @PostMapping
    public Product create(@RequestBody Product p) {
        if (p.getDate() == null) {
            p.setDate(new Date());
        }
        return dao.save(p);
    }

    @PutMapping("{id}")
    public Product update(@PathVariable("id") String id, @RequestBody Product p) {
        return dao.save(p);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") String id) {
        dao.deleteById(id);
    }
}
