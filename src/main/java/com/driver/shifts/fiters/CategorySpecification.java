package com.driver.shifts.fiters;

import org.springframework.data.jpa.domain.Specification;

import com.driver.shifts.entity.Category;
import com.driver.shifts.entity.User;

public class CategorySpecification {
	
	public static Specification<Category> belongsToUser(User user) {
		return (root, _, criteriaBuilder) -> 
			user == null ? null : criteriaBuilder.equal(root.get("user").get("id"), user.getId());
	}
	
	public static Specification<Category> hasName(String name) {
		return (root, _, criteriaBuilder) ->
			name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
	}

}
