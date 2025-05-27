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

import com.driver.shifts.dto.LineDTO;
import com.driver.shifts.exceptions.LineNotFoundException;
import com.driver.shifts.services.LineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lines")
public class LineController {
	
	private final LineService lineService;
	
	public LineController(LineService lineService) {
		this.lineService = lineService;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get All Lines", responses = {
			@ApiResponse(responseCode = "200", description = "All Lines successfully retrieved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<List<LineDTO>> findAll() {
		return ResponseEntity.ok(lineService.findAll());
	}
	
	@GetMapping(value = "/{id:^[1-9]+[0-9]*$}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get Line by Id", responses = {
			@ApiResponse(responseCode = "200", description = "Line successfully retrieved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<LineDTO> get(@PathVariable Long id) {
		return lineService.findById(id)
							  .map(ResponseEntity::ok)
							  .orElseThrow(() -> new LineNotFoundException("Line with id " + id + " not found"));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Saves new Line", responses = {
			@ApiResponse(responseCode = "200", description = "Line successfully saved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	ResponseEntity<LineDTO> create(@RequestBody LineDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(lineService.save(dto));
	}
	
	@PutMapping(value = "/{id:^[1-9]+[0-9]*$}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update Line", responses = {
			@ApiResponse(responseCode = "200", description = "Line successfully updated"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<LineDTO> update(@PathVariable Long id, @Valid @RequestBody LineDTO dto) {
		return ResponseEntity.ok(lineService.update(id, dto));
	}
	
	@DeleteMapping(value = "/{id:^[1-9]+[0-9]*$}")
	@Operation(summary = "Delete Line", responses = {
			@ApiResponse(responseCode = "204", description = "Line successfully deleted"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		lineService.delete(id);
		return ResponseEntity
				.noContent()
				.build();
	}

}
