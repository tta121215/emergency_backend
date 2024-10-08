package com.emergency.rollcall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergency.rollcall.entity.Condition;

@Repository
public interface ConditionDao extends JpaRepository<Condition, Long> {

	@Query("SELECT c FROM Condition c WHERE c.isDelete = 0 and (LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%')) OR LOWER(c.code) LIKE LOWER(CONCAT('%', :param, '%')))")
	Page<Condition> findByNameOrCode(Pageable pageable, @Param("param") String param);
	
	@Query("SELECT c FROM Condition c WHERE c.isDelete = 0")
	Page<Condition> findByNameOrCode(Pageable pageable);
	
	List<Condition> findAllByStatusAndIsDelete(int status, int deleteStatus);
	
	@Query("SELECT COUNT(ec) FROM EmergencyActivate ec WHERE ec.condition.syskey = :syskey and ec.isDelete = 0")
    Long countEmergencyActivatesByConditionId(@Param("syskey") Long syskey);

	@Modifying
	@Transactional
	@Query("UPDATE Condition c SET c.isDefault = :isDefault")
	void updateConditionDefault(@Param("isDefault") int isDefault);
}
