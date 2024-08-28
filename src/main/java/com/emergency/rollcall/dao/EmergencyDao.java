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

import com.emergency.rollcall.entity.Emergency;

@Repository
public interface EmergencyDao extends JpaRepository<Emergency, Long> {

	@Query("SELECT c FROM Emergency c WHERE c.isDelete = 0 and (LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%')) OR LOWER(c.code) LIKE LOWER(CONCAT('%', :param, '%')))")
	Page<Emergency> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM Emergency c WHERE c.isDelete = 0")
	Page<Emergency> findByNameOrCode(Pageable pageable);
	
	List<Emergency> findAllByStatusAndIsDelete(int status, int deleteStatus);
	
	@Query("SELECT COUNT(e) FROM EmergencyActivate e WHERE e.emergency.syskey = :syskey and e.isDelete = 0")
    Long countEmergencyActivatesByEmergencyTypeId(@Param("syskey") Long syskey);

	@Modifying
	@Transactional
	@Query("UPDATE Emergency e SET e.isDefault = :isDefault")
	void updateEmergencyDefault(@Param("isDefault") int isDefault);
}
