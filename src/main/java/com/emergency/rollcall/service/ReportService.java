package com.emergency.rollcall.service;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.DashboardDetailDto;
import com.emergency.rollcall.dto.HeadCountDto;
import com.emergency.rollcall.dto.NotiReadLogDto;
import com.emergency.rollcall.dto.StaffDto;

public interface ReportService {

	Page<DashboardDetailDto> getAllCheckInList(Long activateId, int page, int size, String sortBy, String direction, String params);

	Page<StaffDto> getAllUnCheckInList(Long activateId, int page, int size, String sortBy, String direction, String params);
	
	byte[] exportUnCheckInListToExcelAsByteArray(Long activateId, String params);
	
	byte[] exportCheckInListToExcelAsByteArray(Long activateId, String params);
	
	Page<NotiReadLogDto> searchByParams(int page, int size, String params, String sortBy, String direction, Long emergencyId);
	
	byte[] getAllNotiReadLog(Long activateId, String params);
	
	Page<HeadCountDto> getTotalHeadCountReport(Long activateId, int page, int size, String sortBy, String direction, String params);
}
