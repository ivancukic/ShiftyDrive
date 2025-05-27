package com.driver.shifts.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.driver.shifts.dto.LineDTO;
import com.driver.shifts.dto.UserDTO;
import com.driver.shifts.entity.Line;
import com.driver.shifts.entity.User;
import com.driver.shifts.exceptions.InvalidTimeException;
import com.driver.shifts.repositories.LineRepository;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
	
	@Mock
    private AuthenticatedUserProvider authenticatedUserProvider;
	
	@Mock
	private LineRepository lineRepository;
	
	@InjectMocks
	private LineServiceImpl lineServiceImpl;
	
	
	@Test
	public void testFindAll() {
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());		
		Line line1 = new Line(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
		Line line2 = new Line(2L, "Test Line 2", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
		
		when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2));
		
		List<LineDTO> result = lineServiceImpl.findAll();
		
		assertEquals(2, result.size());
	}
	
	@Test
	public void testFindById() {
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());	
		Line line1 = new Line(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
		
		when(lineRepository.findById(1L)).thenReturn(Optional.of(line1));
		
		Optional<LineDTO> result = lineServiceImpl.findById(1L);
		
		assertTrue(result.isPresent());
	}
	
	@Test
	public void testSave() {
		UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
		LineDTO lineDTO = new LineDTO(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, userDTO);
		
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
		Line line1 = new Line(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
		
		when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);
		when(lineRepository.save(any(Line.class))).thenReturn(line1);
		
		LineDTO result = lineServiceImpl.save(lineDTO);
		
		assertNotNull(result);
	}
	
	@Test
	public void testUpdate() {
		UserDTO userDTO = new UserDTO(1L, "Test User", "test@example.com", new Date(), new Date());
		LineDTO lineDTO = new LineDTO(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, userDTO);
		
		User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
		Line line1 = new Line(1L, "Test Line 1", LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.now(), 2, null, user);
		
		when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);
//		when(lineRepository.findById(1L)).thenReturn(Optional.of(line1));
		when(lineRepository.save(any(Line.class))).thenReturn(line1);
		
		LineDTO result = lineServiceImpl.save(lineDTO);
		
		assertNotNull(result);
		assertEquals("Test Line 1", result.getName());
	}
	
	@Test
	public void testDelete() {
		when(lineRepository.existsById(1L)).thenReturn(true);
		
		lineServiceImpl.delete(1L);
		
		verify(lineRepository, times(1)).deleteById(1L);
	}
	
	@Test
    void testSave_ValidDto_ShouldReturnSavedDto() {
        // Given
        User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
        LineDTO dto = new LineDTO(1L, "Test Line", LocalTime.of(8, 0), LocalTime.of(16, 0), null, 0, null, null);

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);
        when(lineRepository.save(any(Line.class))).thenAnswer(invocation -> {
            Line saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        // When
        LineDTO result = lineServiceImpl.save(dto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getTotal_time());
        assertTrue(result.getNum_of_drivers() > 0);
    }
	
	@Test
    void testSave_NullDto_ShouldReturnNull() {
        assertNull(lineServiceImpl.save(null));
    }
	
	@Test
    void testSave_StartTimeEqualEndTime_ShouldThrowInvalidTimeException() {
//        User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());
        LineDTO dto = new LineDTO(1L, "Test Line", LocalTime.of(8, 0), LocalTime.of(8, 0), null, 0, null, null);

//        when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);

        assertThrows(InvalidTimeException.class, () -> lineServiceImpl.save(dto));
    }
	
	@Test
    void testSave_EndTimeBeforeStartTime_ShouldWrapAroundMidnight() {
        LineDTO dto = new LineDTO(1L, "Night Shift", LocalTime.of(22, 0), LocalTime.of(2, 0), null, 0, null, null);
        User user = new User(1L, "Test User", "test@example.com", new Date(), new Date());

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(user);
        when(lineRepository.save(any(Line.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LineDTO result = lineServiceImpl.save(dto);

        assertNotNull(result.getTotal_time());
        assertTrue(result.getNum_of_drivers() > 0);
    }
	
}
