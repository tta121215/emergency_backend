package com.emergency.rollcall.service;

import com.emergency.rollcall.dto.NotiTemplateDto;

import com.emergency.rollcall.dto.ResponseDto;

public interface NotiTemplateService {

	ResponseDto saveNotiTemplate(NotiTemplateDto notiTemplateDto);

	NotiTemplateDto getById();

	ResponseDto updateNotiTemplate(NotiTemplateDto notiTemplateDto);

	ResponseDto deleteNotiTemplate(long id);

	Boolean getRouteStatus();

//	Page<NotificationDto> searchByParams(int page, int size, String params, String sortBy, String direction);
//	
//	List<NotificationDto> searchByEmergency(long id);

}
