package com.driver.shifts.dto;

public class CategoryDTO {
	
	private Long id;
	
	private String name;
	
	private UserDTO user;

	public CategoryDTO() {

	}
	
	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public CategoryDTO(Long id, String name, UserDTO user) {
		this.id = id;
		this.name = name;
		this.user = user;
	}
	
	private CategoryDTO(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.user = builder.user;
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

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "CategoryDTO [id=" + id + ", name=" + name + ", user=" + user + "]";
	}
	
	// Builder pattern
	public static class Builder {
		private Long id;
		private String name;
		
		private UserDTO user;

		public Builder(Long id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public Builder setUser(UserDTO user) {
			this.user = user;
			return this;
		}

		public CategoryDTO build() {
			return new CategoryDTO(this);
		}
		
	}

}
