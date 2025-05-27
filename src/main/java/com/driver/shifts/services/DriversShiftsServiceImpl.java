package com.driver.shifts.services;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.driver.shifts.dto.DriverDTO;
import com.driver.shifts.dto.DriversShiftsDTO;
import com.driver.shifts.dto.LineDTO;
import com.driver.shifts.entity.DriversShifts;
import com.driver.shifts.entity.enumeration.Shift;
import com.driver.shifts.exceptions.BadRequestAlertException;
import com.driver.shifts.exceptions.DriversShiftsNotFoundException;
import com.driver.shifts.exceptions.SameDriverException;
import com.driver.shifts.exceptions.SameLineException;
import com.driver.shifts.mapper.DriverMapper;
import com.driver.shifts.mapper.DriversShiftsMapper;
import com.driver.shifts.mapper.LineMapper;
import com.driver.shifts.repositories.DriversShiftsRepository;

import jakarta.transaction.Transactional;

@Service
public class DriversShiftsServiceImpl implements DriversShiftsService {
	
	private final DriversShiftsRepository driversShiftsRepository;
	private final AuthenticatedUserProvider authenticatedUserProvider;

	
	public DriversShiftsServiceImpl(DriversShiftsRepository driversShiftsRepository,
			AuthenticatedUserProvider authenticatedUserProvider) {
		this.driversShiftsRepository = driversShiftsRepository;
		this.authenticatedUserProvider = authenticatedUserProvider;
	}

	@Override
	public List<DriversShiftsDTO> findAll() {
		return driversShiftsRepository.findAll()
									  .stream()
									  .map(DriversShiftsMapper::convertToDto)
									  .collect(Collectors.toList());
	}

	@Override
	@Transactional
	public DriversShiftsDTO save(DriversShiftsDTO dto) {
		if(dto==null) {
			return  null;
		}
		
		DriversShifts ds = DriversShiftsMapper.convertToEntity(dto);
		ds.setUser(authenticatedUserProvider.getCurrentUser());
		
		return DriversShiftsMapper.convertToDto(driversShiftsRepository.save(ds));
	}

	@Override
	public Optional<DriversShiftsDTO> findById(Long id) {
		return driversShiftsRepository.findById(id)
									  .map(DriversShiftsMapper::convertToDto);
	}

	@Override
	public DriversShiftsDTO update(Long id, DriversShiftsDTO dto) {
		
		if(dto.getId() == null || !Objects.equals(id, dto.getId())) {
			throw new BadRequestAlertException("Drivers Shifts ID mismatch: Provided ID does not match the entity ID.", "Id is null");
		}
		
		DriversShifts updateDriversShifts = driversShiftsRepository.findById(id).get();
		
		updateDriversShifts.setDriver(DriverMapper.convertToEntity(dto.getDriver()));
		updateDriversShifts.setLine(LineMapper.convertToEntity(dto.getLine()));
		updateDriversShifts.setShift(dto.getShift());
		updateDriversShifts.setUser(authenticatedUserProvider.getCurrentUser());
		
		return DriversShiftsMapper.convertToDto(driversShiftsRepository.save(updateDriversShifts));
	}

	@Override
	public void delete(Long id) {
		if(!driversShiftsRepository.existsById(id)) {
			throw new DriversShiftsNotFoundException("Drivers Shift with id " + id + " not found");
		}
		driversShiftsRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public void assigneShift(LineDTO line, List<DriverDTO> drivers) {
		if(line==null || drivers==null || drivers.isEmpty()) {
			throw new BadRequestAlertException("Line or drivers are null or empty!", "Id is null");
		}
		
		Set<DriverDTO> uniqueDrivers = new HashSet<DriverDTO>(drivers);
		if(uniqueDrivers.size()<drivers.size()) {
			throw new SameDriverException("Duplicate drivers in the same shift assignet!");
		}
		
		Shift[] shifts = Shift.values();
		
		for(int i=0; i<drivers.size(); i++) {
			DriverDTO driverDTO = drivers.get(i);
			String shift = shifts[i].name();
			
			Optional<DriversShifts> existingShift = driversShiftsRepository.ifDriverDrivingSameShift(driverDTO.getId(), shift);
			
			if(existingShift.isPresent()) {
				throw new SameDriverException("Driver " + driverDTO.getId() + " is already assigned to " + shift);
			}
			
			DriversShifts newShift = new DriversShifts();
			
			newShift.setDriver(DriverMapper.convertToEntity(drivers.get(i)));
			newShift.setLine(LineMapper.convertToEntity(line));
			newShift.setShift(shifts[i]);
			newShift.setUser(authenticatedUserProvider.getCurrentUser());
			
			driversShiftsRepository.save(newShift);
		}
	}
	
	@Override
	@Transactional
	public DriversShiftsDTO changeShift(Long id, DriversShiftsDTO driversShiftsDTO, LineDTO line, DriverDTO driver) {
		if(driversShiftsDTO==null || line==null || driver==null) {
			throw new BadRequestAlertException("Line or drivers are null!", "Line,DriverisNull");
		}
				
		DriversShifts shift = driversShiftsRepository.findById(driversShiftsDTO.getId())
													 .orElseThrow(() -> new BadRequestAlertException("Shift not found", "noshift"));
		
		if(Objects.equals(shift.getDriver().getId(), driver.getId())) {
			throw new SameDriverException("You have put same Driver on Shift");
		}
		
		if(!Objects.equals(shift.getLine().getId(), line.getId())) {
			throw new SameLineException("Mismatch between provided line and shift line!");
		}
		
		List<DriversShifts> shiftsOfNewDriver = driversShiftsRepository.findByDriver(DriverMapper.convertToEntity(driver));
		// Better way driversShiftsRepository.existsByDriverAndShift(driverEntity, shift.getShift())
		// instead of looping we are getting boolean
		// i will change later when add date
		
		if(!shiftsOfNewDriver.isEmpty()) {
			for(DriversShifts ds : shiftsOfNewDriver) {
				if(ds.getShift().equals(shift.getShift())) {
					throw new BadRequestAlertException("Driver already assigned to shift " + ds.getShift() + " on another line!", "driveronanothershift");
				}
			}
		}
		
		shift.setDriver(DriverMapper.convertToEntity(driver));
		
		DriversShifts savedShift = driversShiftsRepository.save(shift);
		
		return DriversShiftsMapper.convertToDto(savedShift);
	}

}
