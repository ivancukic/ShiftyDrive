package com.driver.shifts.dto;

import java.util.Date;

public class UserDTO {
	
	private Long id;

	private String fullName;

	private String email;
	
	private Date createdAt;

	private Date updatedAt;

	public UserDTO() {

	}

	public UserDTO(Long id, String fullName, String email, Date createdAt, Date updatedAt) {
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", fullName=" + fullName + ", email=" + email + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
