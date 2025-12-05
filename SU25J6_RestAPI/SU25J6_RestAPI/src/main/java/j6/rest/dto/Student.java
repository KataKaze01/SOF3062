package j6.rest.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "Students")
public class Student {
	@Id
	String id;
	@Column(columnDefinition = "NVARCHAR(50) NOT NULL")
	String name;
	boolean gender;
	double mark;
}
