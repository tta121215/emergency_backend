package com.emergency.rollcall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.Condition;
import com.emergency.rollcall.entity.Menu;
import com.emergency.rollcall.entity.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {

	

}
