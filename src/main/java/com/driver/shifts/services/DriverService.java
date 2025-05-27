package com.driver.shifts.services;

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

}
