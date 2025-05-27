package com.driver.shifts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {
	
	@Id
	@Column(name = "category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "category_name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public Category() {
		
	}

	public Category(Long id, String name, User user) {
		this.id = id;
		this.name = name;
		this.user = user;
	}
	
	public Category(Builder builder) {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", user=" + user + "]";
	}
	
	public static class Builder {
		
		private Long id;
		private String name;

		private User user;
		
		
		public Builder(Long id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public Builder setUser(User user) {
			this.user = user;
			return this;
		}

		public Category build() {
			return new Category(this);
		} 
		
	}

}
