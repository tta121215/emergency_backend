package com.emergency.rollcall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.Notification;

@Repository
public interface NotificationDao extends JpaRepository<Notification, Long> {

}
