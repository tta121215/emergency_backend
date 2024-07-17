package com.emergency.rollcall.service;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.ReNotificationDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface ReNotificationService {

	ResponseDto saveReNotification(ReNotificationDto notiDto);

	ReNotificationDto getById(long id);

	ResponseDto updateReNotification(ReNotificationDto notiDto);

	ResponseDto deleteReNotification(long id);

	Page<ReNotificationDto> searchByParams(int page, int size, String params);

}
