package com.example.student_management_api.service;

import com.example.student_management_api.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getAllStudents();
    Optional<Student> getStudentById(String id);
    Student saveStudent(Student student);
    Student updateStudent(String id, Student studentDetails); // Có thể dùng phương thức khác tùy cách bạn xử lý
    void deleteStudent(String id);
}
