package com.driver.shifts.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driver.shifts.entity.Driver;
import com.driver.shifts.entity.DriversShifts;
import com.driver.shifts.entity.enumeration.Shift;

public interface DriversShiftsRepository extends JpaRepository<DriversShifts, Long> {
	
	public List<DriversShifts> findByDriver(Driver driver);
	
	@Query(value= "SELECT * FROM public.drivers_shifts WHERE driver_id = :driver_id AND shift = :shift FETCH FIRST 1 ROW ONLY", nativeQuery = true)
	Optional<DriversShifts> ifDriverDrivingSameShift(@Param("driver_id") Long driver_id, @Param("shift") String shift);
	
	Optional<DriversShifts> findFirstByDriver_IdAndShift(Long driverId, Shift shift);

}
