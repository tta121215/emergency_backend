package com.emergency.rollcall.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import com.emergency.rollcall.entity.EmergencyActivate;


@Repository
public interface EmergencyActivateDao extends JpaRepository<EmergencyActivate, Long> {

	@Query("SELECT c FROM EmergencyActivate c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :params, '%')) OR LOWER(c.remark) LIKE LOWER(CONCAT('%', :params, '%'))")
	Page<EmergencyActivate> findByNameandRemark(Pageable pageable, @Param("params") String params);
	
	@Query("SELECT c FROM EmergencyActivate c ")
	Page<EmergencyActivate> findByNameandRemark(Pageable pageable);

}
