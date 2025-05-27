package com.driver.shifts.dto;

public class ShiftChangeDTO {
	
	private DriversShiftsDTO driversShifts;
	private LineDTO line;
	private DriverDTO driver;
	
	
	public DriversShiftsDTO getDriversShifts() {
		return driversShifts;
	}
	public void setDriversShifts(DriversShiftsDTO driversShifts) {
		this.driversShifts = driversShifts;
	}
	public LineDTO getLine() {
		return line;
	}
	public void setLine(LineDTO line) {
		this.line = line;
	}
	public DriverDTO getDriver() {
		return driver;
	}
	public void setDriver(DriverDTO driver) {
		this.driver = driver;
	}
	
}
