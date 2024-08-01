package com.emergency.rollcall.service;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.AuditLogDto;


public interface AuditLogService {

	void saveAuditLog(String username,String apiMethod, String details, String ipaddress, String browserVersion);    
	
	
	Page<AuditLogDto> searchByParams(int page, int size, String params, String sortBy, String direction);
	

}
