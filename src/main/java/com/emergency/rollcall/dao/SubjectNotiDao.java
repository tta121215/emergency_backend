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

	@Query("SELECT c FROM SubjectNoti c WHERE c.isDelete = 0 AND LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<SubjectNoti> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM SubjectNoti c WHERE c.isDelete = 0")
	Page<SubjectNoti> findByNameOrCode(Pageable pageable);

	List<SubjectNoti> findAllByStatusAndIsDelete(Integer status,Integer isDelete);

	@Query("SELECT 1 as status from SubjectNoti c where c.name='Route' and c.syskey in :ids ")
	Integer isRouteStatus(@Param("ids") List<Long> ids);

	@Query(value = "SELECT COUNT(*) FROM ERC_noti_template WHERE noti_subject LIKE '%,' || :subjectNotiId || ',%' "
			+ "OR noti_subject LIKE :subjectNotiId || ',%' " + "OR noti_subject LIKE '%,' || :subjectNotiId "
			+ "OR noti_subject = :subjectNotiId", nativeQuery = true)
	Long findNotiTemplatesBySubjectNotiSyskey(@Param("subjectNotiId") long subjectNotiId);

}
