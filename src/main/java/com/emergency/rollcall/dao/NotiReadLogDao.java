package com.emergency.rollcall.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.NotiReadLog;


@Repository
public interface NotiReadLogDao extends JpaRepository<NotiReadLog, Long> {

	@Query("SELECT c FROM NotiReadLog c WHERE LOWER(c.staffId) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<NotiReadLog> findByUserName(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM NotiReadLog c")
	Page<NotiReadLog> findByUserName(Pageable pageable);
}
