package com.driver.shifts.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "lines")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Line {
	
	@Id
	@Column(name = "line_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime start_time;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime end_time;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime total_time;
		
	@Column(name = "number_of_drivers")
	private Integer num_of_drivers;
	
	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DriversShifts> driversShifts = new ArrayList<DriversShifts>();
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public Line() {
		
	}
	
	public Line(Long id, String name, LocalTime start_time, LocalTime end_time, LocalTime total_time,
			Integer num_of_drivers, List<DriversShifts> driversShifts) {
		this.id = id;
		this.name = name;
		this.start_time = start_time;
		this.end_time = end_time;
		this.total_time = total_time;
		this.num_of_drivers = num_of_drivers;
		this.driversShifts = driversShifts;
	}

	public Line(Long id, String name, LocalTime start_time, LocalTime end_time, LocalTime total_time,
			Integer num_of_drivers, List<DriversShifts> driversShifts, User user) {
		this.id = id;
		this.name = name;
		this.start_time = start_time;
		this.end_time = end_time;
		this.total_time = total_time;
		this.num_of_drivers = num_of_drivers;
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

	public LocalTime getStart_time() {
		return start_time;
	}

	public void setStart_time(LocalTime start_time) {
		this.start_time = start_time;
	}

	public LocalTime getEnd_time() {
		return end_time;
	}

	public void setEnd_time(LocalTime end_time) {
		this.end_time = end_time;
	}

	public LocalTime getTotal_time() {
		return total_time;
	}

	public void setTotal_time(LocalTime total_time) {
		this.total_time = total_time;
	}

	public Integer getNum_of_drivers() {
		return num_of_drivers;
	}

	public void setNum_of_drivers(Integer num_of_drivers) {
		this.num_of_drivers = num_of_drivers;
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
		return "Line [id=" + id + ", name=" + name + ", start_time=" + start_time + ", end_time=" + end_time
				+ ", total_time=" + total_time + ", num_of_drivers=" + num_of_drivers + ", driversShifts="
				+ driversShifts + ", user=" + user + "]";
	}

}
