package com.emergency.rollcall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.Emergency;

@Repository
public interface EmergencyDao extends JpaRepository<Emergency, Long> {

}
