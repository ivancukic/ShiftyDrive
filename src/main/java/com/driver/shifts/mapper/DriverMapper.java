package com.driver.shifts.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;

import com.driver.shifts.dto.DriverDTO;
import com.driver.shifts.entity.Driver;

public class DriverMapper {

    public static DriverDTO convertToDto(Driver driver) {
        if (driver == null) {
            return null;
        }
        
        DriverDTO dto = new DriverDTO();
        
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setDob(driver.getDob());
        dto.setActive(driver.getActive());
        dto.setCategory(CategoryMapper.convertToDto(driver.getCategory()));
        dto.setInstructor(convertInstructorToDto(driver.getInstructor()));
        dto.setTrainees(convertTraineesToDto(driver.getTrainees()));
        if(Hibernate.isInitialized(driver.getDriversShifts()) && driver.getDriversShifts() != null) {
			dto.setDriversShifts(driver.getDriversShifts()
								.stream()
								.map(DriversShiftsMapper::convertToDto)
								.collect(Collectors.toList()));
		}
//        dto.setUser(UserMapper.convertToDto(driver.getUser()));

        return dto;
    }

    public static Driver convertToEntity(DriverDTO dto) {
        if (dto == null) {
            return null;
        }

        Driver driver = new Driver();
        driver.setId(dto.getId());
        driver.setName(dto.getName());
        driver.setDob(dto.getDob());
        driver.setActive(dto.getActive());
        driver.setCategory(CategoryMapper.convertToEntity(dto.getCategory()));
        driver.setInstructor(convertInstructorToEntity(dto.getInstructor()));
        driver.setTrainees(convertTraineesToEntity(dto.getTrainees()));
        if(Hibernate.isInitialized(dto.getDriversShifts()) && dto.getDriversShifts() != null) {
			driver.setDriversShifts(dto.getDriversShifts()
								.stream()
								.map(DriversShiftsMapper::convertToEntity)
								.collect(Collectors.toList()));
		}
//        driver.setUser(UserMapper.convertToEntity(dto.getUser()));

        return driver;
    }

    private static DriverDTO convertInstructorToDto(Driver instructor) {
        if (instructor == null || !Hibernate.isInitialized(instructor)) {
            return null;
        }
        // Avoid infinite recursion by not mapping instructor's trainees
        return new DriverDTO(instructor.getId(), instructor.getName(), instructor.getDob(), instructor.getActive(),
                CategoryMapper.convertToDto(instructor.getCategory()), null, null, null, UserMapper.convertToDto(instructor.getUser()));
    }

    private static Driver convertInstructorToEntity(DriverDTO dto) {
        if (!Hibernate.isInitialized(dto) || dto == null) {
            return null;
        }

        Driver instructor = new Driver();
        instructor.setId(dto.getId());
        instructor.setName(dto.getName());
        instructor.setDob(dto.getDob());
        instructor.setActive(dto.getActive());
        instructor.setCategory(CategoryMapper.convertToEntity(dto.getCategory()));
        instructor.setUser(UserMapper.convertToEntity(dto.getUser()));
        return instructor;
    }

    private static List<DriverDTO> convertTraineesToDto(List<Driver> trainees) {
        if (trainees == null || !Hibernate.isInitialized(trainees)) {
            return null;
        }
        return trainees.stream()
                .map(t -> new DriverDTO(t.getId(), t.getName(), t.getDob(), t.getActive(),
                        CategoryMapper.convertToDto(t.getCategory()), null, null, null, UserMapper.convertToDto(t.getUser())))
                .collect(Collectors.toList());
    }

    private static List<Driver> convertTraineesToEntity(List<DriverDTO> trainees) {
        if (trainees == null || !Hibernate.isInitialized(trainees)) {
            return null;
        }

        return trainees.stream()
                .map(dto -> {
                    Driver trainee = new Driver();
                    trainee.setId(dto.getId()); 
                    return trainee;
                })
                .collect(Collectors.toList());
    }
}