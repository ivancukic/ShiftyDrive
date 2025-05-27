package com.driver.shifts.services;

import java.util.List;
import java.util.Optional;

import com.driver.shifts.dto.LineDTO;

public interface LineService {
	
	List<LineDTO> findAll();
	
	LineDTO save(LineDTO dto);
	
	Optional<LineDTO> findById(Long id);
	
	LineDTO update(Long id, LineDTO dto);
	
	void delete(Long id);

}
