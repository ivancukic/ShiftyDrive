package com.driver.shifts.exceptions;

public class BadRequestAlertException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String type;

	public BadRequestAlertException(String message, String type) {
        super(message);
        this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
