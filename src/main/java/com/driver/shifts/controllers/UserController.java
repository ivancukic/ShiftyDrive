package com.driver.shifts.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driver.shifts.dto.UserDTO;
import com.driver.shifts.entity.User;
import com.driver.shifts.mapper.UserMapper;
import com.driver.shifts.services.UserService;

@RequestMapping("/users")
@RestController
public class UserController {
	
	private final UserService userService;
	
//	private final Logger log = LoggerFactory.getLogger(UserController.class);

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/me")
	public ResponseEntity<UserDTO> authenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		User currentUser = (User) authentication.getPrincipal();

		return ResponseEntity.ok(UserMapper.convertToDto(currentUser));
	}
	
	@GetMapping
	public ResponseEntity<List<UserDTO>> allUsers() {
		
		List<User> users = userService.allUsers();
		
		return ResponseEntity.ok(users.stream()
									  .map(UserMapper::convertToDto)
									  .collect(Collectors.toList()));
	}

}
