package com.driver.shifts.mapper;

import java.util.stream.Collectors;

import org.hibernate.Hibernate;

import com.driver.shifts.dto.LineDTO;
import com.driver.shifts.entity.Line;

public class LineMapper {
	
	 	public static LineDTO convertToDto(Line line) {
	        if (line == null) {
	            return null;
	        }
	        
	        LineDTO dto = new LineDTO();
	        
	        dto.setId(line.getId());
	        dto.setName(line.getName());
	        dto.setStart_time(line.getStart_time());
	        dto.setEnd_time(line.getEnd_time());
	        dto.setTotal_time(line.getTotal_time());
	        dto.setNum_of_drivers(line.getNum_of_drivers());
	        if(Hibernate.isInitialized(line.getDriversShifts()) && line.getDriversShifts()!=null) {
				dto.setDriversShifts(line.getDriversShifts()
										 .stream()
										 .map(DriversShiftsMapper::convertToDto)
										 .collect(Collectors.toList()));
			}
//	        dto.setUser(UserMapper.convertToDto(line.getUser()));

	        return dto;
	    }

	    public static Line convertToEntity(LineDTO dto) {
	        if (dto == null) {
	            return null;
	        }

	        Line line = new Line();
	        
	        line.setId(dto.getId());
	        line.setName(dto.getName());
	        line.setStart_time(dto.getStart_time());
	        line.setEnd_time(dto.getEnd_time());
	        line.setTotal_time(dto.getTotal_time());
	        line.setNum_of_drivers(dto.getNum_of_drivers());
	        if(Hibernate.isInitialized(dto.getDriversShifts()) && dto.getDriversShifts()!=null) {
				line.setDriversShifts(dto.getDriversShifts()
										 .stream()
										 .map(DriversShiftsMapper::convertToEntity)
										 .collect(Collectors.toList()));
			}
//	        line.setUser(UserMapper.convertToEntity(dto.getUser()));

	        return line;
	    }

}
