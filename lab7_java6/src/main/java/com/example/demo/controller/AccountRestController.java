package com.example.demo.controller;
import com.example.demo.dao.AccountDAO;
import com.example.demo.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*") @RestController @RequestMapping("/rest/accounts")
public class AccountRestController {
    @Autowired AccountDAO dao;

    @GetMapping() public List<Account> getAll() { return dao.findAll(); }
    @GetMapping("/{id}") public Account getOne(@PathVariable("id") String id) { return dao.findById(id).orElse(null); }
    @PostMapping() public Account create(@RequestBody Account item) { return dao.save(item); }
    @PutMapping("/{id}") public Account update(@PathVariable("id") String id, @RequestBody Account item) { return dao.save(item); }
    @DeleteMapping("/{id}") public void delete(@PathVariable("id") String id) { dao.deleteById(id); }
}