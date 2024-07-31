package com.emergency.rollcall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.ContentNoti;

@Repository
public interface ContentNotiDao extends JpaRepository<ContentNoti, Long> {

	@Query("SELECT c FROM ContentNoti c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<ContentNoti> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM ContentNoti c")
	Page<ContentNoti> findByNameOrCode(Pageable pageable);
	
	List<ContentNoti> findAllByStatus(Integer status);

	@Query("SELECT 1 as status from ContentNoti c where c.name='Route' and c.syskey in :ids")
	Integer isRouteStatus(@Param("ids")List<Long> ids);

}
