package com.emergency.rollcall.service;

import java.util.List;

import com.emergency.rollcall.dto.EmergencyDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface EmergencyService {

	ResponseDto saveEmergency(EmergencyDto emergencyDto);

	EmergencyDto getById(Long id);

	ResponseDto updateEmergency(EmergencyDto emergencyDto);
	
	ResponseDto deleteEmergency(EmergencyDto emergencyDto);
	
	List<EmergencyDto> getAllList();

}
