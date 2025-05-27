package com.driver.shifts.mapper;

import com.driver.shifts.dto.DriversShiftsDTO;
import com.driver.shifts.entity.DriversShifts;

public class DriversShiftsMapper {
	
	public static DriversShiftsDTO convertToDto(DriversShifts ds) {
		if(ds==null) {
			return null;
		}
		
		DriversShiftsDTO dto = new DriversShiftsDTO();
		
		dto.setId(ds.getId());
		dto.setDriver(DriverMapper.convertToDto(ds.getDriver()));
		dto.setLine(LineMapper.convertToDto(ds.getLine()));
		dto.setShift(ds.getShift());
//		dto.setUser(UserMapper.convertToDto(ds.getUser()));		
	
		return dto;
	}
	
	public static DriversShifts convertToEntity(DriversShiftsDTO dto) {
		if(dto==null) {
			return null;
		}
		
		DriversShifts ds = new DriversShifts();
		
		ds.setId(dto.getId());
		ds.setDriver(DriverMapper.convertToEntity(dto.getDriver()));
		ds.setLine(LineMapper.convertToEntity(dto.getLine()));
		ds.setShift(dto.getShift());
//		ds.setUser(UserMapper.convertToEntity(dto.getUser()));		
		
		return ds;
	}

}
