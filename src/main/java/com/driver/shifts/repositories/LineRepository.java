package com.driver.shifts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driver.shifts.entity.Line;

public interface LineRepository extends JpaRepository<Line, Long> {

}
