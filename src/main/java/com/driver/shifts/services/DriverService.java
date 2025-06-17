package com.driver.shifts.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.driver.shifts.dto.DriverDTO;


public interface DriverService {
	
	List<DriverDTO> findAll();
	
	DriverDTO save(DriverDTO dto);
	
	Optional<DriverDTO> findById(Long id);
	
	DriverDTO update(Long id, DriverDTO dto);
	
	void delete(Long id);
	
	List<DriverDTO> getActiveDrivers();
	
	List<DriverDTO> findDriversWithFilterDynamic(String name, Boolean active, LocalDate dobBefore, LocalDate dobAfter, LocalDate dobBetweenStart, LocalDate dobBetweenEnd, Long categoryId);

}
