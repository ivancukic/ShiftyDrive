package com.driver.shifts.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DriverDTO {
	
	private Long id;

	private String name;

	private LocalDate dob;

	private Boolean active;

	private CategoryDTO category;

	private DriverDTO instructor;

	private List<DriverDTO> trainees = new ArrayList<DriverDTO>();
	
	private List<DriversShiftsDTO> driversShifts = new ArrayList<DriversShiftsDTO>();
	
	private UserDTO user;

	public DriverDTO() {

	}
	
	public DriverDTO(Long id, String name, LocalDate dob, Boolean active, CategoryDTO category, DriverDTO instructor,
			List<DriverDTO> trainees, List<DriversShiftsDTO> driversShifts) {
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.active = active;
		this.category = category;
		this.instructor = instructor;
		this.trainees = trainees;
		this.driversShifts = driversShifts;
	}

	public DriverDTO(Long id, String name, LocalDate dob, Boolean active, CategoryDTO category, DriverDTO instructor,
			List<DriverDTO> trainees, List<DriversShiftsDTO> driversShifts, UserDTO user) {
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.active = active;
		this.category = category;
		this.instructor = instructor;
		this.trainees = trainees;
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

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	public DriverDTO getInstructor() {
		return instructor;
	}

	public void setInstructor(DriverDTO instructor) {
		this.instructor = instructor;
	}

	public List<DriverDTO> getTrainees() {
		return trainees;
	}

	public void setTrainees(List<DriverDTO> trainees) {
		this.trainees = trainees;
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
	public boolean equals(Object obj) {
		if(this==obj) return true; 
		if(!(obj instanceof DriverDTO)) return false;
		
		DriverDTO that = (DriverDTO) obj;
		
		return id != null && id.equals(that.id);
	}
	
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "DriverDTO [id=" + id + ", name=" + name + ", dob=" + dob + ", active=" + active + ", category="
				+ category + ", instructor=" + instructor + ", trainees=" + trainees + ", driversShifts="
				+ driversShifts + ", user=" + user + "]";
	}

}
