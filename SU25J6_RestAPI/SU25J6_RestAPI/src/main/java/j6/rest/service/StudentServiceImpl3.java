package j6.rest.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import j6.rest.dto.Student;
import j6.rest.dto.StudentMap;

//@Service
public class StudentServiceImpl3 implements StudentService{
	String api = "https://fpolyedu.firebaseio.com/students/{id}.json";
	RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public List<Student> findAll() {
		var url = api.replace("/{id}", "");
		var map = restTemplate.getForObject(url, StudentMap.class);
		if(map != null) {
			return List.copyOf(map.values());
		}
		return List.of();
	}

	@Override
	public Student findById(String id) {
		var url = api.replace("{id}", id);
		return restTemplate.getForObject(url, Student.class);
	}

	@Override
	public boolean existsById(String id) {
		return (this.findById(id) != null);
	}

	@Override
	public Student create(Student student) {
		var url = api.replace("{id}", student.getId());
		restTemplate.put(url, student, Student.class);
		return student;
	}

	@Override
	public Student update(Student student) {
		var url = api.replace("{id}", student.getId());
		restTemplate.put(url, student, Student.class);
		return student;
	}

	@Override
	public void deleteById(String id) {
		var url = api.replace("{id}", id);
		restTemplate.delete(url);
	}

}
