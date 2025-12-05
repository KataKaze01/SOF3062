package com.example.demo.controller;
import com.example.demo.dao.ProductDAO;
import com.example.demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*") @RestController @RequestMapping("/rest/products")
public class ProductRestController {
    @Autowired ProductDAO dao;

    @GetMapping() public List<Product> getAll() { return dao.findAll(); }
    @GetMapping("/{id}") public Product getOne(@PathVariable("id") Integer id) { return dao.findById(id).orElse(null); }
    @PostMapping() public Product create(@RequestBody Product item) { return dao.save(item); }
    @PutMapping("/{id}") public Product update(@PathVariable("id") Integer id, @RequestBody Product item) { return dao.save(item); }
    @DeleteMapping("/{id}") public void delete(@PathVariable("id") Integer id) { dao.deleteById(id); }
}