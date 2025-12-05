package com.poly.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name= "J6Users")
public class Role {
	@Id
	String id;
	String name;
	@OneToMany(mappedBy = "role")
	List<UserRole> userRoles;
}
