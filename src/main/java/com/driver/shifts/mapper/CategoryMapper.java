package com.driver.shifts.mapper;

import com.driver.shifts.dto.CategoryDTO;
import com.driver.shifts.entity.Category;

public class CategoryMapper {
	
	public static CategoryDTO convertToDto(Category category) {		
		if(category==null) {
			return null;
		}
		
		return new CategoryDTO.Builder(category.getId(), category.getName()).build();
	}
	
	public static Category convertToEntity(CategoryDTO dto) {
		if(dto==null) {
			return null;
		}
		
//		Category category =  new Category();
//		
//		category.setId(dto.getId());
//		category.setName(dto.getName());
//		category.setUser(UserMapper.convertToEntity(dto.getUser()));
		
		return new Category.Builder(dto.getId(), dto.getName()).build();
	}

}
