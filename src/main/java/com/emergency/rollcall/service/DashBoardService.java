package com.emergency.rollcall.service;

import com.emergency.rollcall.dto.DashboardResponseDto;


public interface DashBoardService {
	
	DashboardResponseDto getCheckInCountsByAssemblyPoint(Long emergencyActivateId);
}
