package com.driver.shifts.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.driver.shifts.dto.UserDTO;
import com.driver.shifts.entity.User;
import com.driver.shifts.mapper.UserMapper;
import com.driver.shifts.repositories.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> allUsers() {
		List<User> users = new ArrayList<User>();
		
		userRepository.findAll().forEach(users::add);
		
		return users;
	}
	
	public UserDTO findUserByEmail(String email) {
		return UserMapper.convertToDto(userRepository.findByEmail(email).get());
	} 

}
