package com.driver.shifts.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.driver.shifts.dto.DriversShiftsDTO;
import com.driver.shifts.dto.ShiftChangeDTO;
import com.driver.shifts.dto.ShiftDTO;
import com.driver.shifts.exceptions.DriversShiftsNotFoundException;
import com.driver.shifts.services.DriversShiftsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/drivers-shifts")
public class DriversShiftsController {
	
	private final DriversShiftsService driversShiftsService;

	public DriversShiftsController(DriversShiftsService driversShiftsService) {
		this.driversShiftsService = driversShiftsService;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get All Drivers Shifts", responses = {
			@ApiResponse(responseCode = "200", description = "All Drivers Shifts successfully retrieved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<List<DriversShiftsDTO>> findAll() {
		return ResponseEntity.ok(driversShiftsService.findAll());
	}
	
	@GetMapping(value = "/{id:^[1-9]+[0-9]*$}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get Driver Shift by Id", responses = {
			@ApiResponse(responseCode = "200", description = "Driver Shift successfully retrieved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<DriversShiftsDTO> get(@PathVariable Long id) {
		return driversShiftsService.findById(id)
							  .map(ResponseEntity::ok)
							  .orElseThrow(() -> new DriversShiftsNotFoundException("Driver Shift with id " + id + " not found"));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Saves new Driver Shift", responses = {
			@ApiResponse(responseCode = "200", description = "Driver Shift successfully saved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	ResponseEntity<DriversShiftsDTO> create(@RequestBody DriversShiftsDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(driversShiftsService.save(dto));
	}
	
	@PutMapping(value = "/{id:^[1-9]+[0-9]*$}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update Driver Shift", responses = {
			@ApiResponse(responseCode = "200", description = "Driver Shift successfully updated"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<DriversShiftsDTO> update(@PathVariable Long id, @RequestBody DriversShiftsDTO dto) {
		return ResponseEntity.ok(driversShiftsService.update(id, dto));
	}
	
	@DeleteMapping(value = "/{id:^[1-9]+[0-9]*$}")
	@Operation(summary = "Delete Driver Shift", responses = {
			@ApiResponse(responseCode = "204", description = "Driver Shift successfully deleted"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		driversShiftsService.delete(id);
		return ResponseEntity
				.noContent()
				.build();
	}

	@PostMapping(value = "/new-shift", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Saves new Driver Shift", responses = {
			@ApiResponse(responseCode = "200", description = "Driver Shift successfully saved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<Void> assigneShift(@RequestBody ShiftDTO shift) {
		driversShiftsService.assigneShift(shift.getLine(), shift.getDrivers());
		return ResponseEntity
				.noContent()
				.build();
	}
	
	@PutMapping(value = "/change-shift/{id:^[1-9]+[0-9]*$}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Change Driver Shift", responses = {
			@ApiResponse(responseCode = "200", description = "Driver Shift successfully changed"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<DriversShiftsDTO> changeShift(@PathVariable Long id, @RequestBody ShiftChangeDTO changeDTO) {
		return ResponseEntity.ok(driversShiftsService.changeShift(id, changeDTO.getDriversShifts(), changeDTO.getLine(), changeDTO.getDriver()));
	}
}
