package com.emergency.rollcall.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.Assembly;

@Repository
public interface AssemblyDao extends JpaRepository<Assembly,Long>{

	

	

}
