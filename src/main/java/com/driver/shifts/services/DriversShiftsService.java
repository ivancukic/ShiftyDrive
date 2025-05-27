package com.driver.shifts.services;

import java.util.List;
import java.util.Optional;

import com.driver.shifts.dto.DriverDTO;
import com.driver.shifts.dto.DriversShiftsDTO;
import com.driver.shifts.dto.LineDTO;

public interface DriversShiftsService {
	
	List<DriversShiftsDTO> findAll();
	
	DriversShiftsDTO save(DriversShiftsDTO dto);
	
	Optional<DriversShiftsDTO> findById(Long id);
	
	DriversShiftsDTO update(Long id, DriversShiftsDTO dto);
	
	void delete(Long id); 
	
	void assigneShift(LineDTO line, List<DriverDTO> drivers);
	
	DriversShiftsDTO changeShift(Long id, DriversShiftsDTO driversShiftsDTO, LineDTO line, DriverDTO driver);

}
