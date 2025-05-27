package com.driver.shifts.services;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.driver.shifts.dto.LineDTO;
import com.driver.shifts.entity.Line;
import com.driver.shifts.exceptions.BadRequestAlertException;
import com.driver.shifts.exceptions.InvalidTimeException;
import com.driver.shifts.exceptions.LineNotFoundException;
import com.driver.shifts.exceptions.ResourceNotFoundException;
import com.driver.shifts.mapper.LineMapper;
import com.driver.shifts.repositories.LineRepository;

import jakarta.transaction.Transactional;

@Service
public class LineServiceImpl implements LineService {
	
	private final LineRepository lineRepository;
	private final AuthenticatedUserProvider authenticatedUserProvider;
	private static final String DRIVER_TIME_OF_SHIFT = "08:00:00";
	
	
	public LineServiceImpl(LineRepository lineRepository, AuthenticatedUserProvider authenticatedUserProvider) {
		this.lineRepository = lineRepository;
		this.authenticatedUserProvider = authenticatedUserProvider;
	}

	@Override
	public List<LineDTO> findAll() {
		return lineRepository.findAll()
                .stream()
                .map(LineMapper::convertToDto)
                .collect(Collectors.toList());
	}
	
	@Override
	public Optional<LineDTO> findById(Long id) {
		return lineRepository.findById(id)
								.map(LineMapper::convertToDto);
	}
	
	@Override
	@Transactional
	public LineDTO save(LineDTO dto) {
		if(dto==null) {
			return  null;
		} 
		
		Line saveLine = LineMapper.convertToEntity(dto);
		
		LocalTime workingTime = LocalTime.parse(DRIVER_TIME_OF_SHIFT);
		
		calculateAndSetTotalTime(saveLine, workingTime);
		
		saveLine.setUser(authenticatedUserProvider.getCurrentUser());
		
		return LineMapper.convertToDto(lineRepository.save(saveLine));
	}
	
	@Override
	public LineDTO update(Long id, LineDTO dto) {

		if (dto.getId() == null || !Objects.equals(id, dto.getId())) {
	           throw new BadRequestAlertException("Line ID mismatch: Provided ID does not match the entity ID.", "Id is null");
	    }
		
		Line updatedLine = lineRepository.findById(id)
												.orElseThrow(() -> new ResourceNotFoundException("Line not found with id: " + id));
		
		updatedLine.setName(dto.getName());
		updatedLine.setStart_time(dto.getStart_time());
		updatedLine.setEnd_time(dto.getEnd_time());
		
		LocalTime workingTime = LocalTime.parse(DRIVER_TIME_OF_SHIFT);
		
		calculateAndSetTotalTime(updatedLine, workingTime);
		
		updatedLine.setUser(authenticatedUserProvider.getCurrentUser());

	    return LineMapper.convertToDto(lineRepository.save(updatedLine));
	}

	@Override
	public void delete(Long id) {
		if(!lineRepository.existsById(id)) {
			throw new LineNotFoundException("Line with id " + id + " not found");
		}
		lineRepository.deleteById(id);
	}

	private void calculateAndSetTotalTime(Line line, LocalTime driverDuration) {
		LocalTime startTime = line.getStart_time();
		LocalTime endTime = line.getEnd_time();
		
		if(startTime == null || endTime == null) {
			throw new InvalidTimeException("Start time or end time cannot be null");
		}
		
		if (startTime.equals(endTime)) {
	        throw new InvalidTimeException("Start time and end time cannot be equal");
	    }
		
		long secondsBetween;
		
		if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
		      secondsBetween = java.time.Duration.between(startTime, LocalTime.MAX).getSeconds() 
		                      + 1 
		                      + java.time.Duration.between(LocalTime.MIDNIGHT, endTime).getSeconds();
		} else {
		        secondsBetween = java.time.Duration.between(startTime, endTime).getSeconds();
		}
		
		if(secondsBetween > 24*60*60) {
			 LocalTime invalidTotal = LocalTime.ofSecondOfDay(secondsBetween % (24 * 60 * 60));
		     throw new InvalidTimeException("Total time " + invalidTotal + " exceeds 24 hours.");
		}
		
		LocalTime totalTime = LocalTime.ofSecondOfDay(secondsBetween % (24 * 60 * 60)); 
		
		line.setTotal_time(totalTime);
		
		long totalTimeInSeconds = Duration.between(LocalTime.MIN, totalTime).getSeconds();
		long driverTimeInSeconds = Duration.between(LocalTime.MIN, driverDuration).getSeconds();
		
		int numOfDrivers = (int) Math.ceil((double)totalTimeInSeconds / driverTimeInSeconds);
		
		line.setNum_of_drivers(numOfDrivers);
	}	
}
