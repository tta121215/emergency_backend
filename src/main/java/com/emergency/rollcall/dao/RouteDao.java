package com.emergency.rollcall.dao;

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

}
