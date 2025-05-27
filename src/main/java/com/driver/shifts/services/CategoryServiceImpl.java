package com.driver.shifts.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.driver.shifts.dto.CategoryDTO;
import com.driver.shifts.entity.Category;
import com.driver.shifts.exceptions.BadRequestAlertException;
import com.driver.shifts.exceptions.CategoryNotFoundException;
import com.driver.shifts.exceptions.ResourceNotFoundException;
import com.driver.shifts.mapper.CategoryMapper;
import com.driver.shifts.repositories.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryRepository categoryRepository;
	private final AuthenticatedUserProvider authenticatedUserProvider;
	

	public CategoryServiceImpl(CategoryRepository categoryRepository,
			AuthenticatedUserProvider authenticatedUserProvider) {
		this.categoryRepository = categoryRepository;
		this.authenticatedUserProvider = authenticatedUserProvider;
	}

	@Override
	public List<CategoryDTO> findAll() {
		return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::convertToDto)
                .collect(Collectors.toList());
	}
	
	@Override
	public Optional<CategoryDTO> findById(Long id) {
		return categoryRepository.findById(id)
								.map(CategoryMapper::convertToDto);
	}
	
	@Override
	@Transactional
	public CategoryDTO save(CategoryDTO dto) {	
		if(dto==null) {
			return  null;
		} 

		Category category = CategoryMapper.convertToEntity(dto);
		category.setUser(authenticatedUserProvider.getCurrentUser());
		
		return CategoryMapper.convertToDto(categoryRepository.save(category));
	}
	
	@Override
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {

		if (dto.getId() == null || !Objects.equals(id, dto.getId())) {
	           throw new BadRequestAlertException("Category ID mismatch: Provided ID does not match the entity ID.", "Id is null");
	    }
		
		Category updatedCategory = categoryRepository.findById(id)
												.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
		
		updatedCategory.setName(dto.getName());
		updatedCategory.setUser(authenticatedUserProvider.getCurrentUser());

		updatedCategory = categoryRepository.save(updatedCategory);

	    return CategoryMapper.convertToDto(updatedCategory);
	}

	@Override
	public void delete(Long id) {
		if(!categoryRepository.existsById(id)) {
			throw new CategoryNotFoundException("Category with id " + id + " not found");
		}
		categoryRepository.deleteById(id);
	}


}
