package com.emergency.rollcall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.SubjectNoti;

@Repository
public interface SubjectNotiDao extends JpaRepository<SubjectNoti, Long> {

	@Query("SELECT c FROM SubjectNoti c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<SubjectNoti> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM SubjectNoti c")
	Page<SubjectNoti> findByNameOrCode(Pageable pageable);
	
	List<SubjectNoti> findAllByStatus(Integer status);

}
