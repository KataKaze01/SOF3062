package com.example.student_management_api.repository;

import com.example.student_management_api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    // JpaRepository tự động cung cấp các phương thức CRUD cơ bản như findAll(), save(), findById(), deleteById()
}
