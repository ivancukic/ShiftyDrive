package com.driver.shifts.fiters;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.driver.shifts.entity.Driver;
import com.driver.shifts.entity.User;

public class DriverSpecification {
	
	public static Specification<Driver> belongsToUser(User user) {
		return (root, _, criteriaBuilder) -> 
			user == null ? null : criteriaBuilder.equal(root.get("user").get("id"), user.getId());
	}
	
	public static Specification<Driver> hasName(String name) {
		return (root, _, criteriaBuilder) ->
			name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
	}

	public static Specification<Driver> isActive(Boolean active) {
		return (root, _, criteriaBuilder) -> 
			active == null ? null : criteriaBuilder.equal(root.get("active"), active);
	}
	
	public static Specification<Driver> bornBefore(LocalDate date) {
		return (root, _, criteriaBuilder) ->
			date == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("dob"), date);
	}
	
	public static Specification<Driver> bornAfter(LocalDate date) {
		return (root, _, criteriaBuilder) ->
			date == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("dob"), date);
	}
	
	public static Specification<Driver> bornBetween(LocalDate start, LocalDate end) {
		return (root, _, criteriaBuilder) -> {
			if(start != null && end != null) {
				return criteriaBuilder.between(root.get("dob"), start, end);
			}
			
			if (start != null) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("dob"), start);
			} 
			
			if (end != null) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("dob"), end);
			}
			return null;
		};
	}
	
	public static Specification<Driver> hasCategoryId(Long categoryId) {
		return (root, _, criteriaBuilder) -> 
			categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId);
	}
}
