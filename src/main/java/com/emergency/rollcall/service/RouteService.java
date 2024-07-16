package com.emergency.rollcall.service;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.RouteDto;

public interface RouteService {

	ResponseDto saveRoute(RouteDto routeDto);

	RouteDto getById(long id);

	ResponseDto updateRoute(RouteDto routeDto);

	ResponseDto deleteRoute(long id);

	Page<RouteDto> searchByParams(int page, int size, String params);

}
