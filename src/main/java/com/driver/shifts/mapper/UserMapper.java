package com.driver.shifts.mapper;

import com.driver.shifts.dto.UserDTO;
import com.driver.shifts.entity.User;

public class UserMapper {
	
	public static UserDTO convertToDto(User user) {
		if(user==null) {
			return null;
		}
		
		return new UserDTO(user.getId(), 
						   user.getFullName(), 
						   user.getEmail(), 
						   user.getCreatedAt(), 
						   user.getUpdatedAt());
	}
	
	public static User convertToEntity(UserDTO dto) {
		if(dto==null) {
			return null;
		}
		
		return new User(dto.getId(), 
						dto.getFullName(), 
						dto.getEmail(),  
						dto.getCreatedAt(), 
						dto.getUpdatedAt());
	}

}
