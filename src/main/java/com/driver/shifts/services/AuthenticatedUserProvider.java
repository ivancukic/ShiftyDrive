package com.driver.shifts.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.driver.shifts.entity.User;
import com.driver.shifts.repositories.UserRepository;

@Component
public class AuthenticatedUserProvider {

	private final UserRepository userRepository;

	public AuthenticatedUserProvider(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication==null || !authentication.isAuthenticated()) {
			throw new RuntimeException("Unauthorized");
		}
		
		String email = ((UserDetails) authentication.getPrincipal()).getUsername();
		
		return userRepository.findByEmail(email)
							 .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
}
