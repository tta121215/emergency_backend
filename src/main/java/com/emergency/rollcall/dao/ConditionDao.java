package com.emergency.rollcall.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.Condition;

@Repository
public interface ConditionDao extends JpaRepository<Condition, Long> {

	@Query("SELECT c FROM Condition c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%')) OR LOWER(c.code) LIKE LOWER(CONCAT('%', :param, '%'))")
	Page<Condition> findByNameOrCode(Pageable pageable, @Param("param") String param);

}
