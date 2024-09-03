package com.emergency.rollcall.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.MainBuilding;


@Repository
public interface MainBuildingDao extends JpaRepository<MainBuilding, Long> {

	@Query("SELECT c FROM MainBuilding c WHERE c.isDelete = 0 and LOWER(c.locationName) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<MainBuilding> findByNameOrCode(Pageable pageable, @Param("param") String param);

	@Query("SELECT c FROM MainBuilding c WHERE c.isDelete = 0")
	Page<MainBuilding> findByNameOrCode(Pageable pageable);
	
	List<MainBuilding> findAllByStatusAndIsDelete(int status, int deleteStatus);
	
	

}
