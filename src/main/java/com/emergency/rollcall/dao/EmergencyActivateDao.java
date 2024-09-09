package com.emergency.rollcall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.emergency.rollcall.entity.EmergencyActivate;

@Repository
public interface EmergencyActivateDao extends JpaRepository<EmergencyActivate, Long> {

//	@Query("SELECT c FROM EmergencyActivate c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :params, '%')) OR LOWER(c.remark) LIKE LOWER(CONCAT('%', :params, '%'))")
//	Page<EmergencyActivate> findByNameandRemark(Pageable pageable, @Param("params") String params);
//	
//	@Query("SELECT c FROM EmergencyActivate c ")
//	Page<EmergencyActivate> findByNameandRemark(Pageable pageable);

	@Query("SELECT c FROM EmergencyActivate c " + "LEFT JOIN c.emergency e " + "LEFT JOIN c.condition con "
			+ "WHERE c.isDelete = 0 and (LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%')) "
			+ "OR LOWER(c.remark) LIKE LOWER(CONCAT('%', :param, '%')) "
			+ "OR LOWER(e.name) LIKE LOWER(CONCAT('%', :param, '%')) "
			+ "OR LOWER(con.name) LIKE LOWER(CONCAT('%', :param, '%')))")
	Page<EmergencyActivate> findByNameandRemark(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM EmergencyActivate c " + "LEFT JOIN c.emergency e " + "LEFT JOIN c.condition con WHERE c.isDelete = 0")
	Page<EmergencyActivate> findByNameandRemark(Pageable pageable);

	List<EmergencyActivate> findAllByActivateStatusAndIsDelete(int status, int isDelete);

//	@Query("SELECT c FROM EmergencyActivate c where c.activateStatus = 2")
//	Page<EmergencyActivate> findAllByStatus(Pageable pageable);

	@Query("SELECT e FROM EmergencyActivate e WHERE e.activateStatus = 2 AND e.isDelete = 0 AND "
			+ "(:fromdate IS NULL OR e.activateDate >= :fromdate) AND "
			+ "(:todate IS NULL OR e.activateDate <= :todate) AND "
			+ "(:emergencySyskey IS NULL OR e.emergency.syskey = :emergencySyskey) AND "
			+ "(:conditionSyskey IS NULL OR e.condition.syskey = :conditionSyskey)")
	Page<EmergencyActivate> findAllByStatus(@Param("fromdate") String fromdate,@Param("todate") String todate, @Param("emergencySyskey") Long emergencySyskey,
			@Param("conditionSyskey") Long conditionSyskey, Pageable pageable);

	@Query("SELECT ea FROM EmergencyActivate ea WHERE ea.isDelete = 0 AND ea.activateStatus IN (1,2) AND " + "(:fromDate IS NULL OR ea.activateDate >= :fromDate) AND "
			+ "(:toDate IS NULL OR ea.activateDate <= :toDate) "
			+ "ORDER BY ea.syskey DESC ")
	List<EmergencyActivate> findAllByDateRange(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
	
	List<EmergencyActivate> findAllByActivateStatus(int activateStatus,Sort sort);

}
