package com.driver.shifts.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driver.shifts.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long>, JpaSpecificationExecutor<Driver> {
	
	
	// OVO JE JPQL ZATO KORISTIMO IME CLASE DRIVER I POLJE trainees U NATIVQUERY BISMO MORALI DA PISEM IME TABELE
	@Query("SELECT d FROM Driver d LEFT JOIN FETCH d.trainees WHERE d.id = :id")
	Optional<Driver> findByIdWithTrainees(@Param("id") Long id);
	
	List<Driver> findByActiveTrue();

}
