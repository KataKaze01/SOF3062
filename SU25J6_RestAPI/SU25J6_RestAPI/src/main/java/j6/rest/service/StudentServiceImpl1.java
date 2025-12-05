package j6.rest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import j6.rest.dto.Student;

@Service
public class StudentServiceImpl1 implements StudentService{
	Map<String, Student> students = new HashMap<>(Map.of(
			"SV01", new Student("SV01", "Sinh viên 01", true, 9.5),
			"SV02", new Student("SV02", "Sinh viên 02", false, 4.5),
			"SV03", new Student("SV03", "Sinh viên 03", true, 6.5),
			"SV04", new Student("SV04", "Sinh viên 04", false, 8.5),
			"SV05", new Student("SV05", "Sinh viên 05", true, 7.5)
	));
	
	@Override
	public List<Student> findAll() {
		return List.copyOf(students.values());
	}

	@Override
	public Student findById(String id) {
		return students.get(id);
	}

	@Override
	public boolean existsById(String id) {
		return students.containsKey(id);
	}

	@Override
	public Student create(Student student) {
		return students.put(student.getId(), student);
	}

	@Override
	public Student update(Student student) {
		return students.put(student.getId(), student);
	}

	@Override
	public void deleteById(String id) {
		students.remove(id);
	}
}
