package com.driver.shifts.dto;

import java.util.List;

public class UploadLineResultDTO {
	
	private List<LineDTO> insertedLines;

	private List<String> errorLines;
	

	public UploadLineResultDTO() {
		
	}

	public UploadLineResultDTO(List<LineDTO> insertedLines, List<String> errorLines) {
		this.insertedLines = insertedLines;
		this.errorLines = errorLines;
	}

	public List<LineDTO> getInsertedLines() {
		return insertedLines;
	}

	public void setInsertedLines(List<LineDTO> insertedLines) {
		this.insertedLines = insertedLines;
	}

	public List<String> getErrorLines() {
		return errorLines;
	}

	public void setErrorLines(List<String> errorLines) {
		this.errorLines = errorLines;
	}
	
}
