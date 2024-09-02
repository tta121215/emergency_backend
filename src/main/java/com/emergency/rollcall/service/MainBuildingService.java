package com.emergency.rollcall.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.MainBuildingDto;
import com.emergency.rollcall.dto.LocationDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface MainBuildingService {

	ResponseDto saveMainBuilding(MainBuildingDto locEmergencyDto);

	MainBuildingDto getById(long id);

	ResponseDto updateMainBuilding(MainBuildingDto locEmergencyDto);

	ResponseDto deleteMainBuilding(long id);

	Page<MainBuildingDto> searchByParams(int page, int size, String params, String sortBy, String direction);
	
	List<MainBuildingDto> getAllList();
	
	

}
