package com.poly.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.entity.*;

import com.poly.dao.CategoryDAO;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryRestController {

    @Autowired
    CategoryDAO dao;

    @GetMapping
    public List<Category> getAll() {
        return dao.findAll();
    }

    @GetMapping("{id}")
    public Category getOne(@PathVariable("id") String id) {
        return dao.findById(id).orElse(null);
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return dao.save(category);
    }

    @PutMapping("{id}")
    public Category update(@PathVariable("id") String id, @RequestBody Category category) {
        return dao.save(category);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") String id) {
        dao.deleteById(id);
    }
}

