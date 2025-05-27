package com.driver.shifts.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.driver.shifts.entity.Category;
import com.driver.shifts.entity.Driver;
import com.driver.shifts.entity.User;

@ExtendWith(MockitoExtension.class)
public class DriverRepositoryTest {
	
	@Mock
	private DriverRepository driverRepository;
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private UserRepository userRepository;
	
	@Test
	void shouldSaveAndFindDriver() {
		 User user = new User();
	     user.setId(1L);
	     user.setEmail("test@example.com");

	     Category category = new Category(1L, "Test Cat", user);
	     Driver driver = new Driver(1L, "Test Driver", LocalDate.now(), true, category, null, null, null, user); 

	     when(userRepository.save(any())).thenReturn(user);
	     when(categoryRepository.save(any())).thenReturn(category);
	     when(driverRepository.save(any())).thenReturn(driver);
	     when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

	     userRepository.save(user);
	     categoryRepository.save(category);
	     
	     Driver savedDriver = driverRepository.save(driver);
	     Optional<Driver> foundDriver = driverRepository.findById(savedDriver.getId());

	     assertThat(foundDriver).isPresent();
	     assertThat(foundDriver.get().getName()).isEqualTo("Test Driver");
	     assertThat(foundDriver.get().getCategory().getName()).isEqualTo("Test Cat");
	     assertThat(foundDriver.get().getUser().getEmail()).isEqualTo("test@example.com");
	}

}
