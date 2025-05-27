package com.driver.shifts.dto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LineDTO {

	private Long id;
	
	private String name;
	
	private LocalTime start_time;
	
	private LocalTime end_time;
	
	private LocalTime total_time;
		
	private Integer num_of_drivers;
	
	private List<DriversShiftsDTO> driversShifts = new ArrayList<DriversShiftsDTO>();
	
	private UserDTO user;

	public LineDTO() {

	}
	
	public LineDTO(Long id, String name, LocalTime start_time, LocalTime end_time, LocalTime total_time,
			Integer num_of_drivers, List<DriversShiftsDTO> driversShifts) {
		this.id = id;
		this.name = name;
		this.start_time = start_time;
		this.end_time = end_time;
		this.total_time = total_time;
		this.num_of_drivers = num_of_drivers;
		this.driversShifts = driversShifts;
	}

	public LineDTO(Long id, String name, LocalTime start_time, LocalTime end_time, LocalTime total_time,
			Integer num_of_drivers, List<DriversShiftsDTO> driversShifts, UserDTO user) {
		this.id = id;
		this.name = name;
		this.start_time = start_time;
		this.end_time = end_time;
		this.total_time = total_time;
		this.num_of_drivers = num_of_drivers;
		this.driversShifts = driversShifts;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalTime getStart_time() {
		return start_time;
	}

	public void setStart_time(LocalTime start_time) {
		this.start_time = start_time;
	}

	public LocalTime getEnd_time() {
		return end_time;
	}

	public void setEnd_time(LocalTime end_time) {
		this.end_time = end_time;
	}

	public LocalTime getTotal_time() {
		return total_time;
	}

	public void setTotal_time(LocalTime total_time) {
		this.total_time = total_time;
	}

	public Integer getNum_of_drivers() {
		return num_of_drivers;
	}

	public void setNum_of_drivers(Integer num_of_drivers) {
		this.num_of_drivers = num_of_drivers;
	}

	public List<DriversShiftsDTO> getDriversShifts() {
		return driversShifts;
	}

	public void setDriversShifts(List<DriversShiftsDTO> driversShifts) {
		this.driversShifts = driversShifts;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "LineDTO [id=" + id + ", name=" + name + ", start_time=" + start_time + ", end_time=" + end_time
				+ ", total_time=" + total_time + ", num_of_drivers=" + num_of_drivers + ", driversShifts="
				+ driversShifts + ", user=" + user + "]";
	}
	
}
