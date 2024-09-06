package com.emergency.rollcall.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.LocEmergency;

@Repository
public interface LocEmergencyDao extends JpaRepository<LocEmergency, Long> {

	@Query("SELECT c FROM LocEmergency c WHERE c.isDelete = 0 and LOWER(c.locationName) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<LocEmergency> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM LocEmergency c WHERE c.isDelete = 0")
	Page<LocEmergency> findByNameOrCode(Pageable pageable);
	
	@EntityGraph(attributePaths = {"routeList"})
    Optional<LocEmergency> findById(Long id);
	
	List<LocEmergency> findAllByStatusAndIsDelete(int status, int deleteStatus);
	
	@Query("SELECT a FROM LocEmergency a JOIN a.emergencyActivatesList e WHERE e.syskey = :emergencyActivateId and a.isDelete = 0")
    List<LocEmergency> findByEmergencyActivateId(@Param("emergencyActivateId") Long emergencyActivateId);
	
	@Query(value = "SELECT id, name FROM PYM_LOCATION_ACCESS_LOOKUP", nativeQuery = true)
	List<Object[]> findAllLocationList();
	
	@Query("SELECT COUNT(e) FROM LocEmergency l JOIN l.emergencyActivatesList e WHERE l.syskey = :syskey and e.isDelete = 0")
    Long countEmergencyActivatesByEmergencyLocationId(@Param("syskey") Long syskey);
	
	@Query(value = "SELECT id, name FROM PYM_LOCATION_ACCESS_LOOKUP WHERE id IN (:mainIds)", nativeQuery = true)
    List<Object[]> findByMainIds(@Param("mainIds") List<Long> mainIds);
    
    @Query(value = "SELECT id, name FROM PYM_LOCATION_ACCESS_LOOKUP pla WHERE pla.id NOT IN "
    		+ "(SELECT location_Id FROM ERC_Main_Building eml WHERE eml.is_delete = 0 and eml.status = 1)", nativeQuery = true)
	List<Object[]> findAllMainBuildingList();

}
