package j6.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import j6.rest.dao.StudentDAO;
import j6.rest.dto.Student;

//@Service
public class StudentServiceImpl2 implements StudentService{
	@Autowired
	StudentDAO dao;
	
	@Override
	public List<Student> findAll() {
		return dao.findAll();
	}

	@Override
	public Student findById(String id) {
		return dao.findById(id).get();
	}

	@Override
	public boolean existsById(String id) {
		return dao.existsById(id);
	}

	@Override
	public Student create(Student student) {
		return dao.save(student);
	}

	@Override
	public Student update(Student student) {
		return dao.save(student);
	}

	@Override
	public void deleteById(String id) {
		dao.deleteById(id);
	}

}
