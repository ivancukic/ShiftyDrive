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

import com.driver.shifts.dto.CategoryDTO;
import com.driver.shifts.exceptions.CategoryNotFoundException;
import com.driver.shifts.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get All Categories", responses = {
			@ApiResponse(responseCode = "200", description = "All Categories successfully retrieved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<List<CategoryDTO>> findAll() {
		return ResponseEntity.ok(categoryService.findAll());
	}
	
	@GetMapping(value = "/{id:^[1-9]+[0-9]*$}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get Category by Id", responses = {
			@ApiResponse(responseCode = "200", description = "Category successfully retrieved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<CategoryDTO> get(@PathVariable Long id) {
		return categoryService.findById(id)
							  .map(ResponseEntity::ok)
							  .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found"));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Saves new Category", responses = {
			@ApiResponse(responseCode = "200", description = "Category successfully saved"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(dto));
	}
	
	@PutMapping(value = "/{id:^[1-9]+[0-9]*$}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update Category", responses = {
			@ApiResponse(responseCode = "200", description = "Category successfully updated"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
		return ResponseEntity.ok(categoryService.update(id, dto));
	}
	
	@DeleteMapping(value = "/{id:^[1-9]+[0-9]*$}")
	@Operation(summary = "Delete Category", responses = {
			@ApiResponse(responseCode = "204", description = "Category successfully deleted"),
			@ApiResponse(responseCode = "400", description = "Bad request forwarded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Server error", content = @Content)
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		categoryService.delete(id);
		return ResponseEntity
				.noContent()
				.build();
	}

}
