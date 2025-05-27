package com.driver.shifts.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driver.shifts.dto.LoginResponse;
import com.driver.shifts.dto.LoginUserDto;
import com.driver.shifts.dto.RefreshTokenRequest;
import com.driver.shifts.dto.RegisterUserDto;
import com.driver.shifts.dto.UserDTO;
import com.driver.shifts.entity.RefreshToken;
import com.driver.shifts.entity.User;
import com.driver.shifts.services.AuthenticationService;
import com.driver.shifts.services.JwtService;
import com.driver.shifts.services.RefreshTokenService;

import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	
	private final JwtService jwtService;
	private final AuthenticationService authenticationService;
	private final UserDetailsService userDetailsService;
	private final RefreshTokenService refreshTokenService;
	
	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserDetailsService userDetailsService, RefreshTokenService refreshTokenService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
		this.userDetailsService = userDetailsService;
		this.refreshTokenService = refreshTokenService;
	}
	
	/*
	 * {
		    "email": "john.doe@user.com",
		    "password": "john123",
		    "fullName": "John Doe"
		}
	 */
	@PostMapping("/signup")
	public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterUserDto dto) {
		
		UserDTO registeredUser = authenticationService.signup(dto);
		
		return ResponseEntity.ok(registeredUser);
	}
	
	/*
	 * {
		    "email": "john.doe@user.com",
		    "password": "john123"
		}
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto dto) {
		
		User authenticatedUser = authenticationService.authenticate(dto);
		
		String jwtToken = jwtService.generateToken(authenticatedUser);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticatedUser.getEmail());
		
		LoginResponse loginResponse = new LoginResponse()
				.setToken(jwtToken)
				.setRefreshToken(refreshToken.getToken())
				.setExpiresIn(jwtService.getExpirationTime());
		
		return ResponseEntity.ok(loginResponse);
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
		String requestRefreshToken = request.getRefreshToken();

	    RefreshToken storedToken = refreshTokenService.findByToken(requestRefreshToken);

	    if (storedToken == null || refreshTokenService.isRefreshTokenExpired(storedToken)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                             .body("Refresh token is invalid or expired");
	    }

	    UserDetails userDetails = userDetailsService.loadUserByUsername(storedToken.getUserEmail());

	    refreshTokenService.deleteRefreshToken(requestRefreshToken);

	    String newAccessToken = jwtService.generateToken(userDetails);
	    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

	    return ResponseEntity.ok(
	        new LoginResponse()
	            .setToken(newAccessToken)
	            .setRefreshToken(newRefreshToken.getToken())
	            .setExpiresIn(jwtService.getExpirationTime())
	    );
	}

}
