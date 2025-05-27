package com.driver.shifts.entity;


import com.driver.shifts.entity.enumeration.Shift;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "drivers_shifts")
public class DriversShifts {
	
	@Id
	@Column(name = "ds_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "driver_id")
	private Driver driver;
	
	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;
	
	@Column(name = "shift")
	@Enumerated(EnumType.STRING)
	private Shift shift;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public DriversShifts() {

	}

	public DriversShifts(Long id, Driver driver, Line line, Shift shift) {
		this.id = id;
		this.driver = driver;
		this.line = line;
		this.shift = shift;
	}

	public DriversShifts(Long id, Driver driver, Line line, Shift shift, User user) {
		this.id = id;
		this.driver = driver;
		this.line = line;
		this.shift = shift;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public Shift getShift() {
		return shift;
	}

	public void setShift(Shift shift) {
		this.shift = shift;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "DriversShifts [id=" + id + ", driver=" + driver + ", line=" + line + ", shift=" + shift + ", user="
				+ user + "]";
	}

}
