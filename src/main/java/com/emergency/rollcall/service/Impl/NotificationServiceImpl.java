package com.emergency.rollcall.service.Impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.emergency.rollcall.dao.NotificationDao;
import com.emergency.rollcall.dto.NotificationDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.Notification;
import com.emergency.rollcall.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveNotification(NotificationDto notiDto) {
		ResponseDto res = new ResponseDto();
		Notification notification = new Notification();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		notification = modelMapper.map(notiDto, Notification.class);

		notification.setCreateddate(this.yyyyMMddFormat(strCreatedDate));

		if (notification.getSyskey() == 0) {
			Notification entityres = notificationDao.save(notification);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Saved");
			}
		} else {
			Notification entityres = notificationDao.save(notification);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
			}
		}

		return res;
	}

	@Override
	public NotificationDto getById(long id) {
		NotificationDto conditionDto = new NotificationDto();
		Notification notification = new Notification();
		Optional<Notification> notificationOptional = notificationDao.findById(id);
		if (notificationOptional.isPresent()) {
			notification = notificationOptional.get();
			conditionDto = modelMapper.map(notification, NotificationDto.class);
		}
		return conditionDto;
	}

	@Override
	public ResponseDto updateNotification(NotificationDto notiDto) {
		ResponseDto res = new ResponseDto();
		Notification notification = new Notification();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		Optional<Notification> notiOptional = notificationDao.findById(notiDto.getSyskey());
		if (notiOptional.isPresent()) {
			notification = notiOptional.get();
			notification = modelMapper.map(notiDto, Notification.class);
			notification.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
			notificationDao.save(notification);
			res.setStatus_code(200);
			res.setMessage("Successfully Updated");
		} else {
			res.setStatus_code(401);
			res.setMessage("Data does not found");
		}

		return res;
	}

	@Override
	public ResponseDto deleteNotification(long id) {
		ResponseDto res = new ResponseDto();
		Notification notification = new Notification();

		Optional<Notification> notiOptional = notificationDao.findById(id);
		if (notiOptional.isPresent()) {
			notification = notiOptional.get();
			notificationDao.delete(notification);
			res.setStatus_code(200);
			res.setMessage("Successfully Deleted");
		} else {
			res.setStatus_code(401);
			res.setMessage("No data found");
		}

		return res;
	}

	@Override
	public Page<NotificationDto> searchByParams(int page, int size, String params) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Notification> notiList;
		List<NotificationDto> notiDtoList = new ArrayList<>();
		if (params == null || params.isEmpty()) {
			notiList = notificationDao.findByNotisubject(pageRequest);
			if (notiList != null) {
				for (Notification notification : notiList) {
					NotificationDto notiDto = new NotificationDto();
					notiDto = modelMapper.map(notification, NotificationDto.class);
					notiDtoList.add(notiDto);
				}
			}
		} else {
			notiList = notificationDao.findByNotisubject(pageRequest, params);
			if (notiList != null) {
				for (Notification notification : notiList) {
					NotificationDto notiDto = new NotificationDto();
					notiDto = modelMapper.map(notification, NotificationDto.class);
					notiDtoList.add(notiDto);
				}
			}
		}

		return new PageImpl<>(notiDtoList, pageRequest, notiList.getTotalElements());
	}

	public String ddMMyyyFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null)
			l_Date = aDate.substring(6) + "-" + aDate.substring(4, 6) + "-" + aDate.substring(0, 4);

		return l_Date;
	}

	public String yyyyMMddFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null)
			l_Date = aDate.replaceAll("-", "");

		return l_Date;
	}
}
