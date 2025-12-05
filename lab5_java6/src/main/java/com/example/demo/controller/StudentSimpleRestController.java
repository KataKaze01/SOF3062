package com.example.demo.controller;

import com.example.demo.db.Database;
import com.example.demo.entity.Student;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@CrossOrigin("*")
@RestController
public class StudentSimpleRestController {

    // 1. Lấy tất cả (GET /simple/students)
    @GetMapping("/simple/students")
    public Collection<Student> findAll() {
        // Trả về danh sách các giá trị (Student) đang có trong Map
        return Database.map.values();
    }

    // 2. Lấy một sinh viên (GET /simple/students/{key})
    @GetMapping("/simple/students/{key}")
    public Student findByKey(@PathVariable("key") String key) {
        return Database.map.get(key);
    }

    // 3. Thêm mới (POST /simple/students)
    @PostMapping("/simple/students")
    public Student create(@RequestBody Student student) {
        // Nếu client không gửi ID, tự tạo ID ngẫu nhiên đơn giản
        if (student.getId() == null || student.getId().isEmpty()) {
            String newKey = "SV" + (Database.map.size() + 1);
            student.setId(newKey);
        }
        // Lưu vào Map
        Database.map.put(student.getId(), student);
        return student;
    }

    // 4. Cập nhật (PUT /simple/students/{key})
    @PutMapping("/simple/students/{key}")
    public Student update(@PathVariable("key") String key, @RequestBody Student student) {
        student.setId(key); // Đảm bảo ID khớp với Key trên URL
        Database.map.put(key, student); // Ghi đè vào Map
        return student;
    }

    // 5. Xóa (DELETE /simple/students/{key})
    @DeleteMapping("/simple/students/{key}")
    public void delete(@PathVariable("key") String key) {
        Database.map.remove(key); // Xóa khỏi Map
    }
}