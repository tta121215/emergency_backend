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
import com.emergency.rollcall.dao.RenotificationDao;
import com.emergency.rollcall.dto.ReNotificationDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.Renotification;
import com.emergency.rollcall.service.ReNotificationService;

@Service
public class ReNotificationServiceImpl implements ReNotificationService {

	@Autowired
	private RenotificationDao renotificationDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveReNotification(ReNotificationDto notiDto) {
		ResponseDto res = new ResponseDto();
		Renotification renotification = new Renotification();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		renotification = modelMapper.map(notiDto, Renotification.class);

		renotification.setCreateddate(this.yyyyMMddFormat(strCreatedDate));

		if (renotification.getSyskey() == 0) {
			Renotification entityres = renotificationDao.save(renotification);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Saved");
			}
		} else {
			Renotification entityres = renotificationDao.save(renotification);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
			}
		}

		return res;
	}

	@Override
	public ReNotificationDto getById(long id) {
		ReNotificationDto conditionDto = new ReNotificationDto();
		Renotification ReNotification = new Renotification();
		Optional<Renotification> notificationOptional = renotificationDao.findById(id);
		if (notificationOptional.isPresent()) {
			ReNotification = notificationOptional.get();
			conditionDto = modelMapper.map(ReNotification, ReNotificationDto.class);
		}
		return conditionDto;
	}

	@Override
	public ResponseDto updateReNotification(ReNotificationDto notiDto) {
		ResponseDto res = new ResponseDto();
		Renotification renotification = new Renotification();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		Optional<Renotification> notiOptional = renotificationDao.findById(notiDto.getSyskey());
		if (notiOptional.isPresent()) {
			renotification = notiOptional.get();
			renotification = modelMapper.map(notiDto, Renotification.class);
			renotification.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
			renotificationDao.save(renotification);
			res.setStatus_code(200);
			res.setMessage("Successfully Updated");
		} else {
			res.setStatus_code(401);
			res.setMessage("Data does not found");
		}

		return res;
	}

	@Override
	public ResponseDto deleteReNotification(long id) {
		ResponseDto res = new ResponseDto();
		Renotification renotification = new Renotification();

		Optional<Renotification> renotiOptional = renotificationDao.findById(id);
		if (renotiOptional.isPresent()) {
			renotification = renotiOptional.get();
			renotificationDao.delete(renotification);
			res.setStatus_code(200);
			res.setMessage("Successfully Deleted");
		} else {
			res.setStatus_code(401);
			res.setMessage("No data found");
		}

		return res;
	}

	@Override
	public Page<ReNotificationDto> searchByParams(int page, int size, String params) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Renotification> notiList;
		List<ReNotificationDto> notiDtoList = new ArrayList<>();
		if (params == null || params.isEmpty()) {
			notiList = renotificationDao.findByNameOrCode(pageRequest);
			if (notiList != null) {
				for (Renotification renotification : notiList) {
					ReNotificationDto notiDto = new ReNotificationDto();
					notiDto = modelMapper.map(renotification, ReNotificationDto.class);
					notiDtoList.add(notiDto);
				}
			}
		} else {
			notiList = renotificationDao.findByNameOrCode(pageRequest, params);
			if (notiList != null) {
				for (Renotification notification : notiList) {
					ReNotificationDto notiDto = new ReNotificationDto();
					notiDto = modelMapper.map(notification, ReNotificationDto.class);
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
