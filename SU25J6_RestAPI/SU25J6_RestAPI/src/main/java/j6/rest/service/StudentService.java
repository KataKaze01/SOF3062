package j6.rest.service;

import java.util.List;

import j6.rest.dto.Student;

public interface StudentService {
	/** Truy vấn tất cả sinh viên */
	List<Student> findAll();
	/** Truy vấn sinh viên theo mã */
	Student findById(String id);
	/** Kiểm tra sự tồn tại theo mã */
	boolean existsById(String id);
	/** Tạo sinh viên mới */
	Student create(Student data);
	/** Cập nhật sinh viên */
	Student update(Student data);
	/** Xóa sinh viên theo mã */
	void deleteById(String id);
}