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

import com.poly.dao.UserDAO;
import com.poly.entity.User;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserRestController {

    @Autowired
    UserDAO dao;

    @GetMapping
    public List<User> getAll() {
        return dao.findAll();
    }

    @GetMapping("{username}")
    public User getOne(@PathVariable String username) {
        return dao.findById(username).orElse(null);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return dao.save(user);
    }

    @PutMapping("{username}")
    public User update(@PathVariable String username, @RequestBody User user) {
        return dao.save(user);
    }

    @DeleteMapping("{username}")
    public void delete(@PathVariable String username) {
        dao.deleteById(username);
    }
}

