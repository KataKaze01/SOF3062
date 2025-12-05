package j6.rest.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import j6.rest.dto.Student;

public interface StudentDAO extends JpaRepository<Student, String>{
}
