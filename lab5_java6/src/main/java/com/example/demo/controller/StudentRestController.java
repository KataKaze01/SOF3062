package com.example.demo.controller;

import com.example.demo.dao.StudentDAO;
import com.example.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*") // Cho phép truy cập từ các domain khác (quan trọng nếu ghép với Frontend Lab 4)
@RestController
public class StudentRestController {

    @Autowired
    StudentDAO dao;

    // 1. Lấy tất cả sinh viên
    @GetMapping("/rest/students")
    public List<Student> getAll() {
        return dao.findAll();
    }

    // 2. Lấy một sinh viên theo ID
    @GetMapping("/rest/students/{id}")
    public Student getOne(@PathVariable("id") String id) {
        return dao.findById(id).orElse(null);
    }

    // 3. Thêm mới sinh viên
    @PostMapping("/rest/students")
    public Student post(@RequestBody Student student) {
        dao.save(student);
        return student;
    }

    // 4. Cập nhật sinh viên
    @PutMapping("/rest/students/{id}")
    public Student put(@PathVariable("id") String id, @RequestBody Student student) {
        dao.save(student);
        return student;
    }

    // 5. Xóa sinh viên
    @DeleteMapping("/rest/students/{id}")
    public void delete(@PathVariable("id") String id) {
        dao.deleteById(id);
    }
}