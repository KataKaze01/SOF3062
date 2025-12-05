package com.example.demo.db;

import com.example.demo.entity.Student;
import java.util.HashMap;
import java.util.Map;

public class Database {
    // Map này đóng vai trò như bảng J6Students trong CSDL
    public static Map<String, Student> map = new HashMap<>();

    // Khối static này chạy ngay khi ứng dụng bật, tương đương việc INSERT dữ liệu mẫu
    static {
        map.put("SV001", new Student("SV001", "Nguyễn Văn Tèo", true, 8.5));
        map.put("SV002", new Student("SV002", "Phạm Thị Nở", false, 5.5));
        map.put("SV003", new Student("SV003", "Trần Chí Phèo", true, 3.0));
    }
}