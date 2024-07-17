package com.emergency.rollcall.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.Emergency;

@Repository
public interface EmergencyDao extends JpaRepository<Emergency, Long> {

	@Query("SELECT c FROM Emergency c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%')) OR LOWER(c.code) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<Emergency> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM Emergency c ")
	Page<Emergency> findByNameOrCode(Pageable pageable);

}
