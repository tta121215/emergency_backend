package com.emergency.rollcall.service;

import java.util.List;

import com.emergency.rollcall.dto.NotificationDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface NotificationService {

	ResponseDto saveNotification(NotificationDto notiDto);

	NotificationDto getById(Long id);

	ResponseDto updateNotification(NotificationDto notiDto);

	ResponseDto deleteNotification(NotificationDto notiDto);

	List<NotificationDto> getAllList();

}
