package com.example.demo.controller;
import com.example.demo.dao.CategoryDAO;
import com.example.demo.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*") @RestController @RequestMapping("/rest/categories")
public class CategoryRestController {
    @Autowired CategoryDAO dao;

    @GetMapping() public List<Category> getAll() { return dao.findAll(); }
    @GetMapping("/{id}") public Category getOne(@PathVariable("id") String id) { return dao.findById(id).orElse(null); }
    @PostMapping() public Category create(@RequestBody Category item) { return dao.save(item); }
    @PutMapping("/{id}") public Category update(@PathVariable("id") String id, @RequestBody Category item) { return dao.save(item); }
    @DeleteMapping("/{id}") public void delete(@PathVariable("id") String id) { dao.deleteById(id); }
}