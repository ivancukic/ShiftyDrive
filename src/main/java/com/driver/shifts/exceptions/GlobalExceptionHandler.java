package com.driver.shifts.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail errorDetail;
        
        logger.error("Error occurred: ", exception);

        if (exception instanceof BadCredentialsException) {
            errorDetail = createErrorDetail(HttpStatus.UNAUTHORIZED, "The username or password is incorrect");
        } else if (exception instanceof AccountStatusException) {
            errorDetail = createErrorDetail(HttpStatus.FORBIDDEN, "The account is locked");
        } else if (exception instanceof AccessDeniedException) {
            errorDetail = createErrorDetail(HttpStatus.FORBIDDEN, "You are not authorized to access this resource");
        } else if (exception instanceof SignatureException) {
            errorDetail = createErrorDetail(HttpStatus.FORBIDDEN, "The JWT signature is invalid");
        } else if (exception instanceof ExpiredJwtException) {
            errorDetail = createErrorDetail(HttpStatus.FORBIDDEN, "The JWT token has expired");
        } else {
            errorDetail = createErrorDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown internal server error.");
        }

        return errorDetail;
    }

    private ProblemDetail createErrorDetail(HttpStatus status, String description) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, description);
        problemDetail.setProperty("description", description);
        return problemDetail;
    }
    
    @ExceptionHandler(BadRequestAlertException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestAlertException exception) {
	    Map<String, Object> errorResponse = new HashMap<>();
	    errorResponse.put("timestamp", LocalDateTime.now());
	    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
	    errorResponse.put("error", "Bad Request");
	    errorResponse.put("message", exception.getMessage());
	    errorResponse.put("type", exception.getType());

	    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
    
    @ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException exception) {
		Map<String, Object> errorResponse = new HashMap<String, Object>();
		
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("status", HttpStatus.NOT_FOUND.value());
		errorResponse.put("error", "Resource Not Found");
		errorResponse.put("message", exception.getMessage());
		
		return new ResponseEntity<Map<String,Object>>(errorResponse, HttpStatus.NOT_FOUND);
	}
    
    @ExceptionHandler(CategoryNotFoundException.class)
	@ResponseStatus(code = org.springframework.http.HttpStatus.NOT_FOUND)
	public String handleCategorykNotFoundException(CategoryNotFoundException exception) {
		return exception.getMessage();
	}
    
    @ExceptionHandler(DriverNotFoundException.class)
	@ResponseStatus(code = org.springframework.http.HttpStatus.NOT_FOUND)
	public String handleDriverNotFoundException(DriverNotFoundException exception) {
		return exception.getMessage();
	}
    
    @ExceptionHandler(LineNotFoundException.class)
	@ResponseStatus(code = org.springframework.http.HttpStatus.NOT_FOUND)
	public String handleLineNotFoundException(LineNotFoundException exception) {
		return exception.getMessage();
	}
    
    @ExceptionHandler(InvalidTimeException.class)
	@ResponseStatus(code = org.springframework.http.HttpStatus.BAD_REQUEST)
	public String handleInvalidTimeException(InvalidTimeException exception) {
		return exception.getMessage();
	}
    
    @ExceptionHandler(DriversShiftsNotFoundException.class)
	@ResponseStatus(code = org.springframework.http.HttpStatus.NOT_FOUND)
	public String handleDriversShiftsNotFoundException(DriversShiftsNotFoundException exception) {
		return exception.getMessage();
	}
    
    @ExceptionHandler(SameDriverException.class)
    public ResponseEntity<Map<String, String>> handleSameDriverException(SameDriverException ex) {
    	Map<String, String> response = new HashMap<String, String>();
    	response.put("error", "SameDriverException");
    	response.put("message", ex.getMessage());
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DriverOnSameShiftDiffLineException.class)
    public ResponseEntity<Map<String, String>> handleDriverOnSameShiftDiffLineException(DriverOnSameShiftDiffLineException ex) {
    	Map<String, String> response = new HashMap<String, String>();
    	response.put("error", "DriverOnSameShiftDiffLineException");
    	response.put("message", ex.getMessage());
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidCredentialsException.class) 
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
    	Map<String, Object> error = new HashMap<String, Object>();
    	 error.put("timestamp", LocalDateTime.now());
    	 error.put("status", 400);
    	 error.put("error", "Bad Request");
    	 error.put("type", "INVALID_CREDENTIALS");
    	 error.put("message", ex.getMessage());
    	 
    	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}

