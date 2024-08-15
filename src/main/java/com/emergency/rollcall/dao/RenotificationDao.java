package com.emergency.rollcall.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.Renotification;

@Repository
public interface RenotificationDao extends JpaRepository<Renotification, Long> {

	@Query("SELECT c FROM Renotification c WHERE c.isDelete = 0 AND LOWER(c.time) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<Renotification> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM Renotification c WHERE c.isDelete = 0")
	Page<Renotification> findByNameOrCode(Pageable pageable);

}
