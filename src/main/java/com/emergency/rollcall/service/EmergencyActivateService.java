package com.emergency.rollcall.service;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.EmergencyActivateDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface EmergencyActivateService {

	ResponseDto saveEmergencyActivate(EmergencyActivateDto eActivateDto);

	EmergencyActivateDto getById(long id);

	ResponseDto updateEmergencyActivate(EmergencyActivateDto eActivateDto);

	ResponseDto deleteEmergencyActivate(long id);

	Page<EmergencyActivateDto> searchByParams(int page, int size, String params, String sortBy, String direction);

}
