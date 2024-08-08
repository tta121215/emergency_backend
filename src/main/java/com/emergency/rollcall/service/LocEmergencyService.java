package com.emergency.rollcall.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.LocEmergencyDto;
import com.emergency.rollcall.dto.LocationDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface LocEmergencyService {

	ResponseDto saveLocEmergency(LocEmergencyDto locEmergencyDto);

	LocEmergencyDto getById(long id);

	ResponseDto updateLocEmergency(LocEmergencyDto locEmergencyDto);

	ResponseDto deleteLocEmergency(long id);

	Page<LocEmergencyDto> searchByParams(int page, int size, String params, String sortBy, String direction);
	
	List<LocEmergencyDto> getAllList();
	
	List<LocationDto> getAllLocationList();

}
