package com.driver.shifts.dto;

import com.driver.shifts.entity.enumeration.Shift;

public class DriversShiftsDTO {
	
	private Long id;
	
	private DriverDTO driver;
	
	private LineDTO line;

	private Shift shift;

	private UserDTO user;

	public DriversShiftsDTO() {

	}
	
	public DriversShiftsDTO(Long id, DriverDTO driver, LineDTO line, Shift shift) {
		this.id = id;
		this.driver = driver;
		this.line = line;
		this.shift = shift;
	}

	public DriversShiftsDTO(Long id, DriverDTO driver, LineDTO line, Shift shift, UserDTO user) {
		this.id = id;
		this.driver = driver;
		this.line = line;
		this.shift = shift;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DriverDTO getDriver() {
		return driver;
	}

	public void setDriver(DriverDTO driver) {
		this.driver = driver;
	}

	public LineDTO getLine() {
		return line;
	}

	public void setLine(LineDTO line) {
		this.line = line;
	}

	public Shift getShift() {
		return shift;
	}

	public void setShift(Shift shift) {
		this.shift = shift;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "DriversShiftsDTO [id=" + id + ", driver=" + driver + ", line=" + line + ", shift=" + shift + ", user="
				+ user + "]";
	}

}
