package com.emergency.rollcall.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import com.emergency.rollcall.entity.EmergencyActivate;


@Repository
public interface EmergencyActivateDao extends JpaRepository<EmergencyActivate, Long> {

	@Query("SELECT n FROM Notification n WHERE LOWER(n.notisubject) LIKE LOWER(CONCAT('%', :params, '%'))")
	Page<EmergencyActivate> findByNotisubject(Pageable pageable, @Param("params") String params);
	
	@Query("SELECT n FROM Notification n")
	Page<EmergencyActivate> findByNotisubject(Pageable pageable);

}
