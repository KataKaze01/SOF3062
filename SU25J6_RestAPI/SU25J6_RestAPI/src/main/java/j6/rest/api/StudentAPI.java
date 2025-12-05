package j6.rest.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import j6.rest.dto.Student;
import j6.rest.service.StudentService;

@RestController
public class StudentAPI {
	@Autowired
	StudentService studentService;
	
	@ResponseBody
	@GetMapping("/api/students")
	public Object getAll() {
		return studentService.findAll();
	}
	
	@GetMapping("/api/students/{id}")
	public Object getById(@PathVariable("id") String id) {
		return studentService.findById(id);
	}
	
	@PostMapping("/api/students")
	public Object post(@RequestBody Student student) {
		if(!studentService.existsById(student.getId())) {
			return studentService.create(student);
		}
		throw new RuntimeException(student.getId() + " is in used!");
	}
	
	@PutMapping("/api/students/{id}")
	public Object put(@PathVariable("id") String id, @RequestBody Student student) {
		if(studentService.existsById(id)) {
			return studentService.update(student);
		}
		throw new RuntimeException(id+ " not found!");
	}
	
	@DeleteMapping("/api/students/{id}")
	public void delete(@PathVariable("id") String id) {
		if(studentService.existsById(id)) {
			studentService.deleteById(id);
		} else {
			throw new RuntimeException(id+ " not found!");
		}
	}
}
