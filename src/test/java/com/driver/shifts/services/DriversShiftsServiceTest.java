package com.driver.shifts.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.driver.shifts.dto.DriversShiftsDTO;
import com.driver.shifts.dto.LineDTO;
import com.driver.shifts.dto.UserDTO;
import com.driver.shifts.entity.Category;
import com.driver.shifts.entity.Driver;
import com.driver.shifts.entity.DriversShifts;
import com.driver.shifts.entity.Line;
import com.driver.shifts.entity.User;
import com.driver.shifts.entity.enumeration.Shift;
import com.driver.shifts.repositories.DriversShiftsRepository;

@ExtendWith(MockitoExtension.class)
public class DriversShiftsServiceTest {
	
	@Mock
	private DriversShiftsRepository driversShiftsRepository;
	
	@Mock
    private AuthenticatedUserProvider authenticatedUserProvider;
	
	@InjectMocks
	private DriversShiftsServiceImpl driversShiftsServiceImpl;
	
	
	@Test
	public void testFindAll() {
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());		
		Category category = new Category(1L, "Test Black", user);
		Driver driver1 = new Driver(1L, "Test Drive 1", LocalDate.now(), true, category, null, null, null, user);
		Driver driver2 = new Driver(2L, "Test Drive 2", LocalDate.now(), true, category, null, null, null, user);
		Line line1 = new Line(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
		Line line2 = new Line(2L, "Test Line 2", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
		DriversShifts ds1 = new DriversShifts(1L, driver1, line1, Shift.FIRST_SHIFT, user);
		DriversShifts ds2 = new DriversShifts(2L, driver2, line2, Shift.SECOND_SHIFT, user);
		
		when(driversShiftsRepository.findAll()).thenReturn(Arrays.asList(ds1, ds2));
		
		List<DriversShiftsDTO> result = driversShiftsServiceImpl.findAll();
		
		assertEquals(2, result.size());
	}
	
	@Test
	public void testFindById() {
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());	
		Category category = new Category(1L, "Test Black", user);
		Driver driver1 = new Driver(1L, "Test Drive 1", LocalDate.now(), true, category, null, null, null, user);
		Line line1 = new Line(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
		DriversShifts ds1 = new DriversShifts(1L, driver1, line1, Shift.FIRST_SHIFT, user);
		
		when(driversShiftsRepository.findById(1L)).thenReturn(Optional.of(ds1));
		
		Optional<DriversShiftsDTO> result = driversShiftsServiceImpl.findById(1L);
		
		assertTrue(result.isPresent());
	}
	
	@Test
	public void testSave() {
		UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
		CategoryDTO categoryDTO = new CategoryDTO(1L, "Test Black", userDTO);
		DriverDTO driverDTO = new DriverDTO(1L, "Test Drive", LocalDate.now(), true, categoryDTO, null, null, null, userDTO);
		LineDTO lineDTO = new LineDTO(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, userDTO);
		DriversShiftsDTO driversShiftsDTO = new DriversShiftsDTO(1L, driverDTO, lineDTO, Shift.FIRST_SHIFT, userDTO);
		
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
		Category category = new Category(1L, "Test Black", user);
		Driver driver = new Driver(1L, "Test Drive", LocalDate.now(), true, category, null, null, null, user);
		Line line1 = new Line(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
		DriversShifts ds1 = new DriversShifts(1L, driver, line1, Shift.FIRST_SHIFT, user);
		
		when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);
		when(driversShiftsRepository.save(any(DriversShifts.class))).thenReturn(ds1);
		
		DriversShiftsDTO result = driversShiftsServiceImpl.save(driversShiftsDTO);
		
		assertNotNull(result);
	}
	
	@Test
	public void testUpdate() {
		UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
		CategoryDTO categoryDTO = new CategoryDTO(1L, "Test Black", userDTO);
		DriverDTO driverDTO = new DriverDTO(1L, "Test Drive", LocalDate.now(), true, categoryDTO, null, null, null, userDTO);
		LineDTO lineDTO = new LineDTO(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, userDTO);
		DriversShiftsDTO driversShiftsDTO = new DriversShiftsDTO(1L, driverDTO, lineDTO, Shift.FIRST_SHIFT, userDTO);
		
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
		Category category = new Category(1L, "Test Black", user);
		Driver driver = new Driver(2L, "Test Drive", LocalDate.now(), true, category, null, null, null, user);
		Line line1 = new Line(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
		DriversShifts ds1 = new DriversShifts(1L, driver, line1, Shift.FIRST_SHIFT, user);
		
		when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);
//		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
//		when(driverRepository.findById(2L)).thenReturn(Optional.of(driver));
		when(driversShiftsRepository.save(any(DriversShifts.class))).thenReturn(ds1);
		
		DriversShiftsDTO result = driversShiftsServiceImpl.save(driversShiftsDTO);
		
		assertNotNull(result);
	}
	
	@Test
	public void testDelete() {
		when(driversShiftsRepository.existsById(1L)).thenReturn(true);
		
		driversShiftsServiceImpl.delete(1L);
		
		verify(driversShiftsRepository, times(1)).deleteById(1L);
	}

}
