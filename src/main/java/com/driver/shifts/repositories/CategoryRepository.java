package com.driver.shifts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.driver.shifts.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
