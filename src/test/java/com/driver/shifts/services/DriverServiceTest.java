package com.driver.shifts.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import com.driver.shifts.dto.DriverDTO;
import com.driver.shifts.dto.UserDTO;
import com.driver.shifts.entity.Category;
import com.driver.shifts.entity.Driver;
import com.driver.shifts.entity.User;
import com.driver.shifts.repositories.CategoryRepository;
import com.driver.shifts.repositories.DriverRepository;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
    private AuthenticatedUserProvider authenticatedUserProvider;
	
	@Mock
	private DriverRepository driverRepository;
	
	@InjectMocks
	private DriverServiceImpl driverServiceImpl;
	
	
	@Test
	public void testFindAll() {
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());		
		Category category = new Category(1L, "Test Black", user);
		Driver instructor = new Driver(1L, "Test Inst", LocalDate.now(), true, category, null, null, null, user);
		Driver driver = new Driver(2L, "Test Drive", LocalDate.now(), true, category, instructor, null, null, user);
		
		when(driverRepository.findAll()).thenReturn(Arrays.asList(instructor, driver));
		
		List<DriverDTO> result = driverServiceImpl.findAll();
		
		assertEquals(2, result.size());
	}
	
	@Test
	public void testFindById() {
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());	
		Category category = new Category(1L, "Test Black", user);
		Driver driver = new Driver(2L, "Test Drive", LocalDate.now(), true, category, null, null, null, user);
		
		when(driverRepository.findByIdWithTrainees(2L)).thenReturn(Optional.of(driver));
		
		Optional<DriverDTO> result = driverServiceImpl.findById(2L);
		
		assertTrue(result.isPresent());
	}
	
	@Test
	public void testSave() {
		UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
		CategoryDTO categoryDTO = new CategoryDTO(1L, "Test Black", userDTO);
		DriverDTO driverDTO = new DriverDTO(2L, "Test Drive", LocalDate.now(), true, categoryDTO, null, null, null, userDTO);
		
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
		Category category = new Category(1L, "Test Black", user);
		Driver driver = new Driver(2L, "Test Drive", LocalDate.now(), true, category, null, null, null, user);
		
		when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(driverRepository.save(any(Driver.class))).thenReturn(driver);
		
		DriverDTO result = driverServiceImpl.save(driverDTO);
		
		assertNotNull(result);
	}

	@Test
	public void testUpdate() {
		UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
		CategoryDTO categoryDTO = new CategoryDTO(1L, "Test Black", userDTO);
		DriverDTO driverDTO = new DriverDTO(2L, "Test Drive", LocalDate.now(), true, categoryDTO, null, null, null, userDTO);
		
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
		Category category = new Category(1L, "Test Black", user);
		Driver driver = new Driver(2L, "Test Drive", LocalDate.now(), true, category, null, null, null, user);
		
		when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(driverRepository.findById(2L)).thenReturn(Optional.of(driver));
		when(driverRepository.save(any(Driver.class))).thenReturn(driver);
		
		DriverDTO result = driverServiceImpl.update(2L,driverDTO);
		
		assertNotNull(result);
		assertEquals("Test Drive", result.getName());
	}
	
	@Test
	public void testDelete() {
		when(driverRepository.existsById(1L)).thenReturn(true);
		
		driverServiceImpl.delete(1L);
		
		verify(categoryRepository, times(1)).deleteById(1L);
	}

}
