package com.emergency.rollcall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

	User findByUsername(String username);
}
