package com.driver.shifts.exceptions;

public class DriverOnSameShiftDiffLineException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DriverOnSameShiftDiffLineException(String message) {
		super(message);
	}

}
