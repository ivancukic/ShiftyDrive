package com.driver.shifts.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.driver.shifts.entity.User;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Test
	void shouldSaveAndFindUser() {
		 User user = new User();
	     user.setId(1L);
	     user.setEmail("test@example.com");
	     
	     when(userRepository.save(any())).thenReturn(user);
	     when(userRepository.findById(1L)).thenReturn(Optional.of(user));

	     User savedUser = userRepository.save(user);
	     Optional<User> foundUser = userRepository.findById(savedUser.getId());

	     assertThat(foundUser).isPresent();
	     assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
	}

}
