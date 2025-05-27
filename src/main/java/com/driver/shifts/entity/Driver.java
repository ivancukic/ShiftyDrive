package com.driver.shifts.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "drivers")
public class Driver {
	
	@Id
	@Column(name = "driver_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "driver_name")
	private String name;
	
	@Column(name = "date_of_birth")
	private LocalDate dob;
	
	@Column(name = "active_driver")
	private Boolean active;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", referencedColumnName = "category_id")
	private Category category;
	
	@JsonBackReference
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "instructor_id", referencedColumnName = "driver_id", nullable = true)
	private Driver instructor;
	
	@JsonManagedReference
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "instructor_trainees",
        joinColumns = @JoinColumn(name = "instructor_id"),
        inverseJoinColumns = @JoinColumn(name = "trainee_id"))
    private List<Driver> trainees = new ArrayList<>();
	
	@OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DriversShifts> driversShifts = new ArrayList<DriversShifts>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public Driver() {
	
	}
	
	public Driver(Long id, String name, LocalDate dob, Boolean active, Category category, Driver instructor,
			List<Driver> trainees, List<DriversShifts> driversShifts) {
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.active = active;
		this.category = category;
		this.instructor = instructor;
		this.trainees = trainees;
		this.driversShifts = driversShifts;
	}

	public Driver(Long id, String name, LocalDate dob, Boolean active, Category category, Driver instructor,
			List<Driver> trainees, List<DriversShifts> driversShifts, User user) {
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.active = active;
		this.category = category;
		this.instructor = instructor;
		this.trainees = trainees;
		this.driversShifts = driversShifts;
		this.user = user;
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

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Driver getInstructor() {
		return instructor;
	}

	public void setInstructor(Driver instructor) {
		this.instructor = instructor;
	}

	public List<Driver> getTrainees() {
		return trainees;
	}

	public void setTrainees(List<Driver> trainees) {
		this.trainees = trainees;
	}

	public List<DriversShifts> getDriversShifts() {
		return driversShifts;
	}

	public void setDriversShifts(List<DriversShifts> driversShifts) {
		this.driversShifts = driversShifts;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Driver [id=" + id + ", name=" + name + ", dob=" + dob + ", active=" + active + ", category=" + category
				+ ", instructor=" + instructor + ", trainees=" + trainees + ", driversShifts=" + driversShifts
				+ ", user=" + user + "]";
	}

}
