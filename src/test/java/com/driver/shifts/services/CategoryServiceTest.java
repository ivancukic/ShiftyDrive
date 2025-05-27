package com.driver.shifts.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.driver.shifts.dto.CategoryDTO;
import com.driver.shifts.dto.UserDTO;
import com.driver.shifts.entity.Category;
import com.driver.shifts.entity.User;
import com.driver.shifts.repositories.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
    private AuthenticatedUserProvider authenticatedUserProvider;
	
	@InjectMocks
	private CategoryServiceImpl categoryServiceImpl;

	
	@Test
	public void testFindAll() {
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
		
		Category category1 = new Category(1L, "Test Black", user);
		Category category2 = new Category(2L, "Test Cat", user);
		
		when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));
		
		List<CategoryDTO> result = categoryServiceImpl.findAll();
		
		assertEquals(2, result.size());
	}
	
	@Test
	public void testFindById() {
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
		
		Category category1 = new Category(1L, "Test Black", user);
		
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
		
		Optional<CategoryDTO> result = categoryServiceImpl.findById(1L);
		
		assertTrue(result.isPresent());
	}
	
	@Test
	public void testSave() {
		UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
		CategoryDTO categoryDTO = new CategoryDTO(1L, "Test Black", userDTO);
		
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
		Category category1 = new Category(1L, "Test Black", user);
		
		when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);
		when(categoryRepository.save(any(Category.class))).thenReturn(category1);
		
		CategoryDTO result = categoryServiceImpl.save(categoryDTO);
		
		assertNotNull(result);
	}
	
	@Test
	public void testUpdate() {
		UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
		CategoryDTO categoryDTO = new CategoryDTO(1L, "Test Black", userDTO);
		
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
		Category category1 = new Category(1L, "Test Black", user);
		
		when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
		when(categoryRepository.save(any(Category.class))).thenReturn(category1);
		
		CategoryDTO result = categoryServiceImpl.update(1L, categoryDTO);
		
		assertNotNull(result);
		assertEquals("Test Black", result.getName());
	}
	
	@Test
	public void testDelete() {
		when(categoryRepository.existsById(1L)).thenReturn(true);
		
		categoryServiceImpl.delete(1L);
		
		verify(categoryRepository, times(1)).deleteById(1L);
	}

}
