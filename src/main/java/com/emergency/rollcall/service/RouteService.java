package com.emergency.rollcall.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.RouteDto;

public interface RouteService {

	ResponseDto saveRoute(RouteDto routeDto);

	RouteDto getById(long id);

	ResponseDto updateRoute(RouteDto routeDto);

	ResponseDto deleteRoute(long id);

	Page<RouteDto> searchByParams(int page, int size, String params, String sortBy, String direction);
	
	List<RouteDto> getAllList();
	
	List<RouteDto> getByLocationofEmergency(long id);
	
	ResponseDto saveRouteAttach(String name, String description, Integer status, MultipartFile attachFiles) throws IOException;

}
