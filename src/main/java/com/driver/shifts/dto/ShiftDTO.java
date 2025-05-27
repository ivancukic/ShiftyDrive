package com.driver.shifts.dto;

import java.util.List;

public class ShiftDTO {
	
	private LineDTO line;
	private List<DriverDTO> drivers;
	
	
	public LineDTO getLine() {
		return line;
	}
	public void setLine(LineDTO line) {
		this.line = line;
	}
	public List<DriverDTO> getDrivers() {
		return drivers;
	}
	public void setDrivers(List<DriverDTO> drivers) {
		this.drivers = drivers;
	}

}
