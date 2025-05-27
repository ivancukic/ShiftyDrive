package com.driver.shifts.dto;

import java.util.List;

public class UploadDriverResultDTO {
	
	private List<DriverDTO> insertedDrivers;

	private List<String> errorDrivers;
	

	public UploadDriverResultDTO() {
		
	}

	public UploadDriverResultDTO(List<DriverDTO> insertedDrivers, List<String> errorDrivers) {
		this.insertedDrivers = insertedDrivers;
		this.errorDrivers = errorDrivers;
	}

	public List<DriverDTO> getInsertedDrivers() {
		return insertedDrivers;
	}

	public void setInsertedDrivers(List<DriverDTO> insertedDrivers) {
		this.insertedDrivers = insertedDrivers;
	}

	public List<String> getErrorDrivers() {
		return errorDrivers;
	}

	public void setErrorDrivers(List<String> errorDrivers) {
		this.errorDrivers = errorDrivers;
	}

}
