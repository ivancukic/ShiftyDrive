package com.driver.shifts.dto;

public class LoginResponse {
	
	private String token;
    private String refreshToken;
    private long expiresIn;

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;  
    }

    public LoginResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;  
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;  
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
    
}
