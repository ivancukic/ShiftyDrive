package com.driver.shifts.exceptions;

public class SameDriverException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SameDriverException(String message) {
		super(message);
	}

}
