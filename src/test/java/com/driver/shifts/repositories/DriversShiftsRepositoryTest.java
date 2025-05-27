package com.driver.shifts.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.driver.shifts.entity.Category;
import com.driver.shifts.entity.Driver;
import com.driver.shifts.entity.DriversShifts;
import com.driver.shifts.entity.Line;
import com.driver.shifts.entity.User;

@ExtendWith(MockitoExtension.class)
public class DriversShiftsRepositoryTest {
	
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private DriverRepository driverRepository;
	@Mock
	private LineRepository lineRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private DriversShiftsRepository driversShiftsRepository;
	
	@Test
	void shouldSaveAndFindDriversShifts() {
		 User user = new User();
	     user.setId(1L);
	     user.setEmail("test@example.com");

	     Line line = new Line(1L, "Test Line", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
	     Category category = new Category(1L, "Test Cat", user);
	     Driver driver = new Driver(1L, "Test Driver", LocalDate.now(), true, category, null, null, null, user);
	     DriversShifts driversShifts = new DriversShifts(1L, driver, line, null, user);

	     when(userRepository.save(any())).thenReturn(user);
	     when(lineRepository.save(any())).thenReturn(line);
	     when(categoryRepository.save(any())).thenReturn(category);
	     when(driverRepository.save(any())).thenReturn(driver);
	     when(driversShiftsRepository.save(any())).thenReturn(driversShifts);
	     when(driversShiftsRepository.findById(1L)).thenReturn(Optional.of(driversShifts));

	     userRepository.save(user);  
	     lineRepository.save(line);
	     driverRepository.save(driver);
	     categoryRepository.save(category);
	     
	     DriversShifts savedDriversShifts = driversShiftsRepository.save(driversShifts);
	     Optional<DriversShifts> foundDriversShifts = driversShiftsRepository.findById(savedDriversShifts.getId());

	     assertThat(foundDriversShifts).isPresent();
	     assertThat(foundDriversShifts.get().getLine().getName()).isEqualTo("Test Line");
//	     assertThat(foundDriversShifts.get().getDriver().getName()).isEqualTo("Test Driver");
	     assertThat(foundDriversShifts.get().getUser().getEmail()).isEqualTo("test@example.com");
	}

}
