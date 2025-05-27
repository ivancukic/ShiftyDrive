package com.driver.shifts.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.driver.shifts.dto.DriverDTO;
import com.driver.shifts.dto.LineDTO;
import com.driver.shifts.dto.UploadDriverResultDTO;
import com.driver.shifts.dto.UploadLineResultDTO;

@Service
public class UploadService {

	private final static Logger log = LoggerFactory.getLogger(UploadService.class);
	private final ExecutorService executor = Executors.newFixedThreadPool(2);
	private final LineService lineService;
	private final DriverService driverService;
	

	public UploadService(LineService lineService, DriverService driverService) {
		this.lineService = lineService;
		this.driverService = driverService;
	}

	public UploadLineResultDTO uploadLineNew(final InputStream file) {
		log.info("Entering method uploadLineNew with file");
        System.out.println("Uploading lines from Excel...");
        try {
            UploadLineResultDTO result = parseLineFile(file);

            SecurityContext context = SecurityContextHolder.getContext();

            executor.submit(() -> {
                SecurityContextHolder.setContext(context);
                try {
                    insertLine(result.getInsertedLines());
                    log.info("Async inserted {} lines into DB.", result.getInsertedLines().size());
                } catch (Exception e) {
                    log.error("Failed to insert lines asynchronously: {}", e.getMessage(), e);
                } finally {
                    SecurityContextHolder.clearContext(); 
                }
            });

            log.info("Returning parsed result with {} valid and {} errors.", result.getInsertedLines().size(), result.getErrorLines().size());
            return result;

        } catch (Exception e) {
            log.error("Failed to upload lines: {}", e.getMessage(), e);
            throw new RuntimeException("Line upload failed", e);
        }
    }

    private UploadLineResultDTO parseLineFile(InputStream file) {
    	log.info("Entering method parseLineFile with file");
        UploadLineResultDTO result = new UploadLineResultDTO();
        List<LineDTO> validLines = new ArrayList<>();
        List<String> errorLines = new ArrayList<>();
        XSSFWorkbook workbook = null;

        try {
            workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            if (sheet == null) {
                throw new IllegalArgumentException("No sheet found in uploaded Excel file.");
            }

            for (Row row : sheet) {
                if (rowCount++ == 0) continue; 
                if (row == null) continue;

                LineDTO lineRow = new LineDTO();

                try {
                    Cell nameCell = row.getCell(0);
                    Cell startTimeCell = row.getCell(1);
                    Cell endTimeCell = row.getCell(2);

                    if (nameCell == null || nameCell.getStringCellValue().trim().isEmpty()) {
                        log.warn("Skipping row {}: name is missing", row.getRowNum());
                        errorLines.add("Row " + row.getRowNum() + ": Line name is missing.");
                        continue;
                    }
                    lineRow.setName(nameCell.getStringCellValue().trim());

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                    // Start Time
                    if (startTimeCell != null) {
                        try {
                            if (startTimeCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(startTimeCell)) {
                                lineRow.setStart_time(startTimeCell.getLocalDateTimeCellValue().toLocalTime());
                            } else {
                                String timeStr = startTimeCell.getStringCellValue().trim();
                                lineRow.setStart_time(LocalTime.parse(timeStr, timeFormatter));
                            }
                        } catch (Exception e) {
                            log.warn("Invalid time format at row {} for start time: {}", row.getRowNum(), e.getMessage());
                            errorLines.add("Row " + row.getRowNum() + ": Invalid start time - " + e.getMessage());
                        }
                    }

                    // End Time
                    if (endTimeCell != null) {
                        try {
                            if (endTimeCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(endTimeCell)) {
                                lineRow.setEnd_time(endTimeCell.getLocalDateTimeCellValue().toLocalTime());
                            } else {
                                String timeStr = endTimeCell.getStringCellValue().trim();
                                lineRow.setEnd_time(LocalTime.parse(timeStr, timeFormatter));
                            }
                        } catch (Exception e) {
                            log.warn("Invalid time format at row {} for end time: {}", row.getRowNum(), e.getMessage());
                            errorLines.add("Row " + row.getRowNum() + ": Invalid end time - " + e.getMessage());
                        }
                    }

                    validLines.add(lineRow);

                } catch (Exception rowEx) {
                    log.warn("Failed to parse row {}: {}", row.getRowNum(), rowEx.getMessage());
                    errorLines.add(String.format("Row %d: Failed to parse row due to invalid or missing data.", row.getRowNum()));
                }
            }

        } catch (IOException ioEx) {
            log.error("Failed to read Excel file: {}", ioEx.getMessage(), ioEx);
        } finally {
            try {
                if (workbook != null) workbook.close();
                if (file != null) file.close();
            } catch (IOException closeEx) {
                log.warn("Failed to close stream: {}", closeEx.getMessage());
            }
        }

        result.setInsertedLines(validLines);
        result.setErrorLines(errorLines);
        
        return result;
    }

    private void insertLine(List<LineDTO> lines) {
       log.info("Entering method insertLine with lines size ", lines.size());
       lines.stream()
       		.distinct()
       		.forEach(lineService::save);
    }
    
    // Driver upload
    public UploadDriverResultDTO uploadDriverNew(final InputStream file) {
        log.info("Entering method uploadDriverNew with file");
        System.out.println("Uploading drivers from Excel...");

        try {
        	UploadDriverResultDTO result = parseDriverFile(file);

            SecurityContext context = SecurityContextHolder.getContext();

            CompletableFuture.runAsync(() -> {
                SecurityContextHolder.setContext(context);
                try {
                    insertDriver(result.getInsertedDrivers());
                    log.info("Async inserted {} drivers into DB.", result.getInsertedDrivers().size());
                } catch (Exception e) {
                    log.error("Failed to insert drivers asynchronously: {}", e.getMessage(), e);
                } finally {
                    SecurityContextHolder.clearContext(); 
                }
            }, executor); 

            log.info("Returning parsed result with {} valid and {} errors.",
                     result.getInsertedDrivers().size(), result.getErrorDrivers().size());

            return result;

        } catch (Exception e) {
            log.error("Failed to upload drivers: {}", e.getMessage(), e);
            throw new RuntimeException("Driver upload failed", e);
        }
    }
    
    private UploadDriverResultDTO parseDriverFile(InputStream file) {
    	log.info("Entering method parseDriverFile with file");
    	UploadDriverResultDTO result = new UploadDriverResultDTO();
        List<DriverDTO> validDrivers = new ArrayList<>();
        List<String> errorDrivers = new ArrayList<>();
        XSSFWorkbook workbook = null;

        try {
            workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            if (sheet == null) {
                throw new IllegalArgumentException("No sheet found in uploaded Excel file.");
            }

            for (Row row : sheet) {
                if (rowCount++ == 0) continue; 
                if (row == null) continue;

                DriverDTO driverRow = new DriverDTO();

                try {
                    Cell nameCell = row.getCell(0);
                    Cell dobCell = row.getCell(1);

                    if (nameCell == null || nameCell.getStringCellValue().trim().isEmpty()) {
                        log.warn("Skipping row {}: name is missing", row.getRowNum());
                        errorDrivers.add("Row " + row.getRowNum() + ": Driver name is missing.");
                        continue;
                    }
                    driverRow.setName(nameCell.getStringCellValue().trim());

                    // Date of birth
                    if (dobCell != null) {
                        try {
                            if (dobCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dobCell)) {
                            	driverRow.setDob(dobCell.getLocalDateTimeCellValue().toLocalDate());
                            } else {
                            	String dateStr = dobCell.getStringCellValue().trim();
                                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                driverRow.setDob(LocalDate.parse(dateStr, dateFormatter));
                            }
                        } catch (Exception e) {
                            log.warn("Invalid date format at row {} for date of birth: {}", row.getRowNum(), e.getMessage());
                            errorDrivers.add("Row " + row.getRowNum() + ": Invalid date of birth - " + e.getMessage());
                            continue;
                        }
                    }

                    validDrivers.add(driverRow);

                } catch (Exception rowEx) {
                    log.warn("Failed to parse row {}: {}", row.getRowNum(), rowEx.getMessage());
                    errorDrivers.add(String.format("Row %d: Failed to parse row due to invalid or missing data.", row.getRowNum()));
                }
            }

        } catch (IOException ioEx) {
            log.error("Failed to read Excel file: {}", ioEx.getMessage(), ioEx);
        } finally {
            try {
                if (workbook != null) workbook.close();
                if (file != null) file.close();
            } catch (IOException closeEx) {
                log.warn("Failed to close stream: {}", closeEx.getMessage());
            }
        }

        result.setInsertedDrivers(validDrivers);
        result.setErrorDrivers(errorDrivers);
        
        return result;
    }
    
    private void insertDriver(List<DriverDTO> drivers) {
        log.info("Entering method insertDriver with drivers size ", drivers.size());
        drivers.stream()
        		.distinct()
        		.forEach(driverService::save);
     }
}

