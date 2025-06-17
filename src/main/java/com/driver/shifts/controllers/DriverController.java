package com.driver.shifts.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.driver.shifts.dto.DriverDTO;
import com.driver.shifts.exceptions.DriverNotFoundException;
import com.driver.shifts.services.DriverService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {
	
	private final DriverService driverService;

	public DriverController(DriverService driverService) {
		this.driverService = driverService;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get All Drivers", responses = {
			@ApiResponse(responseCode = "200", description = "All drivers successfully retrieved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<List<DriverDTO>> findAll() {
		return ResponseEntity.ok(driverService.findAll());
	}
	
	@GetMapping(value = "/{id:^[1-9]+[0-9]*$}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get Driver by Id", responses = {
			@ApiResponse(responseCode = "200", description = "Driver successfully retrieved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<DriverDTO> get(@PathVariable Long id) {
		return driverService.findById(id)
							  .map(ResponseEntity::ok)
							  .orElseThrow(() -> new DriverNotFoundException("Driver with id " + id + " not found"));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Saves new Driver", responses = {
			@ApiResponse(responseCode = "200", description = "Driver successfully saved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	ResponseEntity<DriverDTO> create(@RequestBody DriverDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(driverService.save(dto));
	}
	
	@PutMapping(value = "/{id:^[1-9]+[0-9]*$}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update Driver", responses = {
			@ApiResponse(responseCode = "200", description = "Driver successfully updated"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<DriverDTO> update(@PathVariable Long id, @RequestBody DriverDTO dto) {
		return ResponseEntity.ok(driverService.update(id, dto));
	}
	
	@DeleteMapping(value = "/{id:^[1-9]+[0-9]*$}")
	@Operation(summary = "Delete Driver", responses = {
			@ApiResponse(responseCode = "204", description = "Driver successfully deleted"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		driverService.delete(id);
		return ResponseEntity
				.noContent()
				.build();
	}
	
	@GetMapping(value = "/active-drivers", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get All Active Drivers", responses = {
			@ApiResponse(responseCode = "200", description = "All active drivers successfully retrieved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<List<DriverDTO>> getActiveDrivers() {
		return ResponseEntity.ok(driverService.getActiveDrivers());
	}
	
	@GetMapping(value = "/filter-drivers", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Filter drivers", responses = {
			@ApiResponse(responseCode = "200", description = "Drivers are filtered successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<List<DriverDTO>> getDriversWithFilter(
				@RequestParam(required = false) String name,
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dobBefore,
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dobAfter,
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dobBetweenStart,
				@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dobBetweenEnd,
				@RequestParam(required = false) Boolean active,
				@RequestParam(required = false) Long categoryId
			) {
		return ResponseEntity.ok(driverService.findDriversWithFilterDynamic(name, active, dobBefore, dobAfter, dobBetweenStart, dobBetweenEnd, categoryId));
	}
	

}
