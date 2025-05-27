package com.driver.shifts.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.driver.shifts.dto.LoginUserDto;
import com.driver.shifts.dto.RegisterUserDto;
import com.driver.shifts.dto.UserDTO;
import com.driver.shifts.entity.User;
import com.driver.shifts.exceptions.EmailAlreadyUsedException;
import com.driver.shifts.exceptions.InvalidCredentialsException;
import com.driver.shifts.mapper.UserMapper;
import com.driver.shifts.repositories.UserRepository;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	
	
	public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}
	
	
	public UserDTO signup(RegisterUserDto dto) {
		if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
			throw new EmailAlreadyUsedException();
		}
		
		User user = new User();
		
		user.setFullName(dto.getFullName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		return UserMapper.convertToDto(userRepository.save(user));
	}
	
	public User authenticate(LoginUserDto dto) {
		if(dto==null || dto.getEmail()==null || dto.getPassword()==null) {
			throw new InvalidCredentialsException("Email and password must be provided");
		}
		
		if(!isValidEmail(dto.getEmail())) {
			throw new InvalidCredentialsException("Invalid email format");
		}
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
			);

		} catch (BadCredentialsException ex) {
			throw new InvalidCredentialsException("Something wrong with your email or password");
		}
		
		return userRepository.findByEmail(dto.getEmail())
				.orElseThrow(() -> new InvalidCredentialsException("User not found"));	
	}
	
	private boolean isValidEmail(String email) {
		return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	}
}
