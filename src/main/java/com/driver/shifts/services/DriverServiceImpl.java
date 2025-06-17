package com.driver.shifts.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.driver.shifts.dto.DriverDTO;
import com.driver.shifts.entity.Category;
import com.driver.shifts.entity.Driver;
import com.driver.shifts.entity.User;
import com.driver.shifts.exceptions.BadRequestAlertException;
import com.driver.shifts.exceptions.DriverNotFoundException;
import com.driver.shifts.exceptions.ResourceNotFoundException;
import com.driver.shifts.fiters.DriverSpecification;
import com.driver.shifts.mapper.DriverMapper;
import com.driver.shifts.repositories.CategoryRepository;
import com.driver.shifts.repositories.DriverRepository;

import jakarta.transaction.Transactional;

@Service
public class DriverServiceImpl implements DriverService {
	
	private final DriverRepository driverRepository;
	private final CategoryRepository categoryRepository;
	private final AuthenticatedUserProvider authenticatedUserProvider;
	
	
	public DriverServiceImpl(DriverRepository driverRepository, CategoryRepository categoryRepository,
			AuthenticatedUserProvider authenticatedUserProvider) {
		this.driverRepository = driverRepository;
		this.categoryRepository = categoryRepository;
		this.authenticatedUserProvider = authenticatedUserProvider;
	}

	@Override
	public List<DriverDTO> findAll() {
		return driverRepository.findAll()
                .stream()
                .map(DriverMapper::convertToDto) 
                .collect(Collectors.toList());
	}
	
	@Override
	public Optional<DriverDTO> findById(Long id) {
		return driverRepository.findByIdWithTrainees(id)
							   .map(DriverMapper::convertToDto);
	}
	
	@Override
	@Transactional
	public DriverDTO save(DriverDTO dto) {
		if(dto==null) {
			return  null;
		} 

	    Driver driver = DriverMapper.convertToEntity(dto);
	    
	    if(driver.getCategory()!=null) {
	    	Category category = categoryRepository.findById(dto.getCategory().getId())
	    	        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
	    	    driver.setCategory(category);
	    }
	    if (dto.getInstructor() != null && dto.getInstructor().getId() != null) {
	        Driver instructor = driverRepository.findById(dto.getInstructor().getId())
	            .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

	        driver.setInstructor(instructor); 

	        if (instructor.getTrainees() == null) {
	            instructor.setTrainees(new ArrayList<>());
	        }

	        instructor.getTrainees().removeIf(Objects::isNull);

	        if (!instructor.getTrainees().contains(driver)) {
	            instructor.getTrainees().add(driver); 
	        }
	    }
	    if(driver.getActive()==null) {
	    	driver.setActive(true);
	    }
	    driver.setUser(authenticatedUserProvider.getCurrentUser());

	    return DriverMapper.convertToDto(driverRepository.save(driver));
	}
	
	@Override
	@Transactional
	public DriverDTO update(Long id, DriverDTO dto) {
		if (dto.getId() == null || !Objects.equals(id, dto.getId())) {
	           throw new BadRequestAlertException("Driver ID mismatch: Provided ID does not match the entity ID.", "Id is null");
	    }
		
		Driver updatedDriver = driverRepository.findById(id)
												.orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
		
		
		updatedDriver.setName(dto.getName());
		updatedDriver.setDob(dto.getDob());
		updatedDriver.setActive(dto.getActive());
		
		if (dto.getCategory() != null && dto.getCategory().getId() != null) {
		    Category category = categoryRepository.findById(dto.getCategory().getId())
		            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategory().getId()));
		    updatedDriver.setCategory(category);
		}
		
		if (dto.getInstructor() != null && dto.getInstructor().getId() != null) {
		    Driver instructor = driverRepository.findById(dto.getInstructor().getId())
		            .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + dto.getInstructor().getId()));
		    updatedDriver.setInstructor(instructor);
		}
		
		updatedDriver.setTrainees(updateTranees(dto));
		updatedDriver.setUser(authenticatedUserProvider.getCurrentUser());

	    return DriverMapper.convertToDto(driverRepository.save(updatedDriver));
	}

	@Override
	public void delete(Long id) {
		if(!driverRepository.existsById(id)) {
			throw new DriverNotFoundException("Driver with id " + id + " not found");
		}
		driverRepository.deleteById(id);
	}
	
	private List<Driver> updateTranees(DriverDTO dto) {
		List<Driver> updatedTrainees = new ArrayList<Driver>();
		
		if(dto.getTrainees() != null) {
			for(DriverDTO traineeDTO : dto.getTrainees()) {
				if(traineeDTO.getId() != null) {
					Driver trainee = driverRepository.findById(traineeDTO.getId())
							.orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id: " + traineeDTO.getId()));
					updatedTrainees.add(trainee);
				} else {
					updatedTrainees.add(DriverMapper.convertToEntity(traineeDTO));
				}
			}
		}
		return updatedTrainees;
	}

	@Override
	public List<DriverDTO> getActiveDrivers() {		
		return driverRepository.findByActiveTrue()
							   .stream()
							   .map(DriverMapper::convertToDto)
							   .collect(Collectors.toList());
	}

	@Override
	public List<DriverDTO> findDriversWithFilterDynamic(String name, Boolean active, LocalDate dobBefore, LocalDate dobAfter, LocalDate dobBetweenStart, LocalDate dobBetweenEnd, Long categoryId) {
		User currentUser = authenticatedUserProvider.getCurrentUser();
		
		Specification<Driver> spec = Specification
	                .where(DriverSpecification.belongsToUser(currentUser))
	                .and(DriverSpecification.hasName(name))
	                .and(DriverSpecification.isActive(active))
	                .and(DriverSpecification.bornBefore(dobBefore))
	                .and(DriverSpecification.bornAfter(dobAfter))
	                .and(DriverSpecification.bornBetween(dobBetweenStart, dobBetweenEnd))
	                .and(DriverSpecification.hasCategoryId(categoryId));
		
		return driverRepository.findAll(spec)
				               .stream()
				               .map(DriverMapper::convertToDto)
				               .collect(Collectors.toList());
	}

	@Override
	public Page<DriverDTO> findAllDriversWithPagination(Pageable pageable) {
		return driverRepository.findAll(pageable)
  							   .map(DriverMapper::convertToDto);
	}

}
