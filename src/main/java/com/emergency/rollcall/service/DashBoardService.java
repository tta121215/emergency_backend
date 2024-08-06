package com.emergency.rollcall.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.AssemblyCheckInDto;
import com.emergency.rollcall.dto.DashboardDetailDto;
import com.emergency.rollcall.dto.DashboardResponseDto;

public interface DashBoardService {

	DashboardResponseDto getCheckInCountsByAssemblyPoint(Long emergencyActivateId);

	Page<DashboardDetailDto> getByActivateAndAssembly(Long activateId, Long assemblyId, int page, int size);

	Page<DashboardDetailDto> getByActivateId(Long activateId, int page, int size);
}
