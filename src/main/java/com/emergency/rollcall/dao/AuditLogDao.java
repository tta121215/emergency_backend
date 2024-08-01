package com.emergency.rollcall.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.AuditLog;


@Repository
public interface AuditLogDao extends JpaRepository<AuditLog, Long> {

	@Query("SELECT c FROM AuditLog c WHERE LOWER(c.username) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<AuditLog> findByUserName(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM AuditLog c")
	Page<AuditLog> findByUserName(Pageable pageable);
}
