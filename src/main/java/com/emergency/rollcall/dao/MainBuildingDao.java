package com.emergency.rollcall.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.MainBuilding;
import com.emergency.rollcall.entity.Route;


@Repository
public interface MainBuildingDao extends JpaRepository<MainBuilding, Long> {

	@Query("SELECT c FROM MainBuilding c WHERE c.isDelete = 0 and LOWER(c.locationName) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<MainBuilding> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM MainBuilding c WHERE c.isDelete = 0")
	Page<MainBuilding> findByNameOrCode(Pageable pageable);
	
	List<MainBuilding> findAllByStatusAndIsDelete(int status, int deleteStatus);
//	
//	@Query("SELECT r FROM MainBuilding r WHERE r.syskey IN (SELECT e.syskey FROM MainBuilding e JOIN e.locEmergencyList le WHERE le.syskey IN :locEmergencyIds) AND r.isDelete = 0 AND r.status = 1")
//	List<MainBuilding> findAllLocEmergency(@Param("locEmergencyIds") List<Long> locEmergencyIds);
//	
	@Query("SELECT m FROM MainBuilding m WHERE m.locationId IN :mainBuildingIds AND m.isDelete = 0 AND m.status = 1")
	List<MainBuilding> findAllLocEmergency(@Param("mainBuildingIds") List<Long> mainBuildingIds);
}
