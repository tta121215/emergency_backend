package com.emergency.rollcall.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.LocEmergency;

@Repository
public interface LocEmergencyDao extends JpaRepository<LocEmergency, Long> {

	@Query("SELECT c FROM LocEmergency c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<LocEmergency> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM LocEmergency c")
	Page<LocEmergency> findByNameOrCode(Pageable pageable);

}
