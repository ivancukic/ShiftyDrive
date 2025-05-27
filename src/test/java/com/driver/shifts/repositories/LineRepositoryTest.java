package com.driver.shifts.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.driver.shifts.entity.Line;
import com.driver.shifts.entity.User;

@ExtendWith(MockitoExtension.class)
public class LineRepositoryTest {
	
	@Mock
	private LineRepository lineRepository;
	@Mock
	private UserRepository userRepository;
	
	@Test
	void shouldSaveAndFindLine() {
		 User user = new User();
	     user.setId(1L);
	     user.setEmail("test@example.com");

	     Line line = new Line(1L, "Test Line", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);

	     when(userRepository.save(any())).thenReturn(user);
	     when(lineRepository.save(any())).thenReturn(line);
	     when(lineRepository.findById(1L)).thenReturn(Optional.of(line));

	     userRepository.save(user);
	     
	     Line savedLine = lineRepository.save(line);
	     Optional<Line> foundLine = lineRepository.findById(savedLine.getId());

	     assertThat(foundLine).isPresent();
	     assertThat(foundLine.get().getName()).isEqualTo("Test Line");
	     assertThat(foundLine.get().getUser().getEmail()).isEqualTo("test@example.com");
	}

}
