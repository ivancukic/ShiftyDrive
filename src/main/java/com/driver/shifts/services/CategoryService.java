package com.driver.shifts.services;

import java.util.List;
import java.util.Optional;

import com.driver.shifts.dto.CategoryDTO;

public interface CategoryService {
	
	List<CategoryDTO> findAll();
	
	CategoryDTO save(CategoryDTO dto);
	
	Optional<CategoryDTO> findById(Long id);
	
	CategoryDTO update(Long id, CategoryDTO dto);
	
	void delete(Long id);

}
