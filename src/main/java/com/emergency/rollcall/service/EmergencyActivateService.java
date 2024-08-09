package com.emergency.rollcall.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.EActivationDto;
import com.emergency.rollcall.dto.EmergencyActivateDto;
import com.emergency.rollcall.dto.EmergencyRollCallDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface EmergencyActivateService {

	ResponseDto saveEmergencyActivate(EmergencyActivateDto eActivateDto);

	EmergencyActivateDto getById(long id);

	ResponseDto updateEmergencyActivate(EmergencyActivateDto eActivateDto);

	ResponseDto deleteEmergencyActivate(long id);

	Page<EmergencyActivateDto> searchByParams(int page, int size, String params, String sortBy, String direction);
	
	List<EmergencyActivateDto> getAllDashboardList();
	
	List<EmergencyActivateDto> getAllList(String fromDate, String toDate);
	
	EActivationDto emergencyActivate(long id);
	
	ResponseDto emergencyActivateManualEnd(long id);
	
	EActivationDto emergencyActivateForNoti(long id);
	
	Page<EmergencyRollCallDto> emergencyRollCall(String date, Long emergencyType, Long emergencyStatus, int page, int size);




}
