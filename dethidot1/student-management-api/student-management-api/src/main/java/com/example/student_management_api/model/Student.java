package com.example.student_management_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "Students")
@Data // Tự động tạo getter, setter, toString, equals, hashCode
@NoArgsConstructor // Constructor không tham số
@AllArgsConstructor // Constructor với tất cả tham số
public class Student {

    @Id
    @Column(name = "Id", length = 20, nullable = false)
    private String id;

    @Column(name = "Fullname", length = 50, nullable = false)
    private String fullname;

    @Column(name = "Mark", nullable = false)
    private Float mark;

    @Column(name = "Birthday", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate birthday;
}
