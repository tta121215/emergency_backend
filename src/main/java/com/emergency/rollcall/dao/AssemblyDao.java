package com.emergency.rollcall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.Assembly;

@Repository
public interface AssemblyDao extends JpaRepository<Assembly, Long> {

	@Query("SELECT a FROM Assembly a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :param, '%')) OR LOWER(a.latitude) LIKE LOWER(CONCAT('%', :param, '%')) OR LOWER(a.longtiude) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<Assembly> searchByParams(Pageable pageable, @Param("param") String param);

	@Query("SELECT a FROM Assembly a")
	Page<Assembly> searchByParams(Pageable pageable);
	
	List<Assembly> findAllByStatus(int status);
	

	@Query("SELECT a FROM Assembly a JOIN a.emergencyActivatesList e WHERE e.syskey = :emergencyActivateId")
    List<Assembly> findByEmergencyActivateId(@Param("emergencyActivateId") Long emergencyActivateId);
	
	@Query("SELECT COUNT(e) FROM Assembly a JOIN a.emergencyActivatesList e WHERE a.syskey = :syskey")
    Long countEmergencyActivatesByAssemblyId(@Param("syskey") Long syskey);
}
