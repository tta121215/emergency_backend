package com.emergency.rollcall.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.DashboardDetailDto;
import com.emergency.rollcall.dto.DashboardResponseDto;
import com.emergency.rollcall.dto.MalaysiaCalendarDto;
import com.emergency.rollcall.dto.StaffDto;

public interface DashBoardService {

	DashboardResponseDto getCheckInCountsByAssemblyPoint(Long emergencyActivateId);

	Page<DashboardDetailDto> getByActivateAndAssembly(Long activateId, Long assemblyId, int page, int size, String sortBy, String direction,String params);

	Page<DashboardDetailDto> getByActivateId(Long activateId, int page, int size, String sortBy, String direction, String params);

	Page<StaffDto> getAllUnCheckInList(Long activateId, int page, int size, String sortBy, String direction, String params);
	
	String getStaffPhoto(String staffNo);
	
	List<LocalDate> getWeekendsAndWeekdays(LocalDate fromDate, LocalDate  toDate);
	
	MalaysiaCalendarDto getMalaysiaCalendar(LocalDate fromDate, LocalDate  toDate);
}
