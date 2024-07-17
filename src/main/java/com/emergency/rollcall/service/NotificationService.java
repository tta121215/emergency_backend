package com.emergency.rollcall.service;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.NotificationDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface NotificationService {

	ResponseDto saveNotification(NotificationDto notiDto);

	NotificationDto getById(long id);

	ResponseDto updateNotification(NotificationDto notiDto);

	ResponseDto deleteNotification(long id);

	Page<NotificationDto> searchByParams(int page, int size, String params, String sortBy, String direction);

}
