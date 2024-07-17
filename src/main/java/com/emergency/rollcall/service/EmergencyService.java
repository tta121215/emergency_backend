package com.emergency.rollcall.service;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.EmergencyDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface EmergencyService {

	ResponseDto saveEmergency(EmergencyDto emergencyDto);

	EmergencyDto getById(long id);

	ResponseDto updateEmergency(EmergencyDto emergencyDto);

	ResponseDto deleteEmergency(long id);

	Page<EmergencyDto> searchByParams(int page, int size, String params);

}
