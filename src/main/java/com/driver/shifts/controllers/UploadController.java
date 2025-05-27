package com.driver.shifts.controllers;

import java.io.InputStream;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.driver.shifts.dto.UploadDriverResultDTO;
import com.driver.shifts.dto.UploadLineResultDTO;
import com.driver.shifts.services.UploadService;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
	
	private final UploadService uploadService;

	
	public UploadController(UploadService uploadService) {
		this.uploadService = uploadService;
	}
	
	@PostMapping("/line-stage")
    public ResponseEntity<UploadLineResultDTO> uploadLineFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try (InputStream inputStream = file.getInputStream()) {
            UploadLineResultDTO result = uploadService.uploadLineNew(inputStream);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new UploadLineResultDTO(List.of(), List.of("Failed to process file: " + e.getMessage())));
        }
    }
	
	@PostMapping("/driver-stage")
    public ResponseEntity<UploadDriverResultDTO> uploadDriverFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try (InputStream inputStream = file.getInputStream()) {
        	UploadDriverResultDTO result = uploadService.uploadDriverNew(inputStream);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new UploadDriverResultDTO(List.of(), List.of("Failed to process file: " + e.getMessage())));
        }
    }

}
