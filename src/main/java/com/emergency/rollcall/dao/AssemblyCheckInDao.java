package com.emergency.rollcall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.AssemblyCheckIn;

@Repository
public interface AssemblyCheckInDao extends JpaRepository<AssemblyCheckIn, Long> {
	
	@Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id")
	List<AssemblyCheckIn> getAllListByActivationId(@Param("id") Long id);
	
	@Query("SELECT a.assemblyPoint, COUNT(a) FROM AssemblyCheckIn a WHERE a.emergencySyskey = :emergencyActivateSyskey GROUP BY a.assemblyPoint")
    List<Object[]> findCheckInCountsByAssemblyPoint(@Param("emergencyActivateSyskey") Long emergencyActivateSyskey);
    
    @Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :activateId and a.assemblyPoint = :assemblyId ")
	Page<AssemblyCheckIn> getListByAssemblyAndActivate(@Param("activateId") Long activateId, @Param("assemblyId") Long assemblyId, Pageable pageable);
    
    @Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id")
	Page<AssemblyCheckIn> getListByActivationId(@Param("id") Long id, Pageable pageable);
}

