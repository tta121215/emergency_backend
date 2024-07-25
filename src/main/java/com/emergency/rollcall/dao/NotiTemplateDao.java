package com.emergency.rollcall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.NotiTemplate;

@Repository
public interface NotiTemplateDao extends JpaRepository<NotiTemplate, Long> {

	List<NotiTemplate> findAllByStatus(int status);

}
