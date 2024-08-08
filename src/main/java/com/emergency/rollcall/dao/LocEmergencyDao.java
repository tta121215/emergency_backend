package com.emergency.rollcall.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.LocEmergency;

@Repository
public interface LocEmergencyDao extends JpaRepository<LocEmergency, Long> {

	@Query("SELECT c FROM LocEmergency c WHERE LOWER(c.locationName) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<LocEmergency> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM LocEmergency c")
	Page<LocEmergency> findByNameOrCode(Pageable pageable);
	
	@EntityGraph(attributePaths = {"routeList"})
    Optional<LocEmergency> findById(Long id);
	
	List<LocEmergency> findAllByStatus(int status);
	
	@Query("SELECT a FROM LocEmergency a JOIN a.emergencyActivatesList e WHERE e.syskey = :emergencyActivateId")
    List<LocEmergency> findByEmergencyActivateId(@Param("emergencyActivateId") Long emergencyActivateId);
	
	@Query(value = "SELECT id, name FROM PYM_LOCATION_ACCESS_LOOKUP", nativeQuery = true)
	List<Object[]> findAllLocationList();

}
