package com.emergency.rollcall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.ModeNoti;

@Repository
public interface ModeNotiDao extends JpaRepository<ModeNoti, Long> {

	@Query("SELECT c FROM ModeNoti c WHERE c.isDelete = 0 AND LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<ModeNoti> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM ModeNoti c WHERE c.isDelete = 0 ")
	Page<ModeNoti> findByNameOrCode(Pageable pageable);
	
	List<ModeNoti> findAllByStatusAndIsDelete(Integer status, Integer isDelete);
	
	@Query(value = "SELECT COUNT(*) FROM ERC_noti_template WHERE noti_mode LIKE '%,' || :modeNotiId || ',%' "
			+ "OR noti_mode LIKE :modeNotiId || ',%' " + "OR noti_mode LIKE '%,' || :modeNotiId "
			+ "OR noti_mode = :modeNotiId", nativeQuery = true)
	Long findNotiTemplatesByModeNotiSyskey(@Param("modeNotiId") String modeNotiId);

}
