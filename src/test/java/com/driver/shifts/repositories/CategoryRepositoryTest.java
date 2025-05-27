package com.driver.shifts.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.driver.shifts.entity.Category;
import com.driver.shifts.entity.User;

@ExtendWith(MockitoExtension.class)
public class CategoryRepositoryTest {
	
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private UserRepository userRepository;
	
	@Test
	void shouldSaveAndFindCategory() {
		 User user = new User();
	     user.setId(1L);
	     user.setEmail("test@example.com");

	     Category category = new Category(1L, "Test Cat", user);

	     when(userRepository.save(any())).thenReturn(user);
	     when(categoryRepository.save(any())).thenReturn(category);
	     when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

	     userRepository.save(user);
	     
	     Category savedCategory = categoryRepository.save(category);
	     Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

	     assertThat(foundCategory).isPresent();
	     assertThat(foundCategory.get().getName()).isEqualTo("Test Cat");
	     assertThat(foundCategory.get().getUser().getEmail()).isEqualTo("test@example.com");
	}

}
