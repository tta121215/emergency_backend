package com.emergency.rollcall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.Condition;

@Repository
public interface ConditionDao extends JpaRepository<Condition, Long> {

}
