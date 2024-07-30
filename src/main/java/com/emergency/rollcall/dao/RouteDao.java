package com.emergency.rollcall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.Route;

@Repository
public interface RouteDao extends JpaRepository<Route, Long> {

	@Query("SELECT c FROM Route c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<Route> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM Route c")
	Page<Route> findByNameOrCode(Pageable pageable);
	
	List<Route> findAllByStatus(int status);
	
	@Query("SELECT a FROM Route a JOIN a.emergencyActivatesList e WHERE e.syskey = :emergencyActivateId")
    List<Route> findByEmergencyActivateId(@Param("emergencyActivateId") Long emergencyActivateId);
	
//	@Query("SELECT a FROM Route a JOIN Loc_Route" where route <> syskey)
//	List<Route> findByEmergencyLocation(@Param("locEmergencyId") Long locEmergencyId);
	
	
	@Query("SELECT a FROM Route a JOIN a.locEmergencyList e WHERE e.syskey is NULL OR e.syskey <> :locEmergencyId")
	List<Route> findByEmergencyLocaction(@Param("locEmergencyId") Long locEmergencyId);
	
//	@Query("SELECT r FROM Route r WHERE r.syskey NOT IN (SELECT e.syskey FROM Route e JOIN e.locEmergencyList le WHERE le.syskey = :locEmergencyId)")
//	List<Route> findAllExcludingLocEmergency(@Param("locEmergencyId") Long locEmergencyId);
	
	@Query("SELECT r FROM Route r WHERE r.syskey NOT IN (SELECT e.syskey FROM Route e JOIN e.locEmergencyList le WHERE le.syskey IN :locEmergencyIds)")
	List<Route> findAllExcludingLocEmergency(@Param("locEmergencyIds") List<Long> locEmergencyIds);



}
