package com.emergency.rollcall.service;

import java.util.List;

import com.emergency.rollcall.dto.AssemblyCheckInDto;
import com.emergency.rollcall.dto.DashboardResponseDto;

public interface DashBoardService {
	
	DashboardResponseDto getCheckInCountsByAssemblyPoint(Long emergencyActivateId);
	
	List<AssemblyCheckInDto> getByActivateAndAssembly(Long activateId, Long assemblyId);
	
	List<AssemblyCheckInDto> getByActivateId(Long activateId);
}
