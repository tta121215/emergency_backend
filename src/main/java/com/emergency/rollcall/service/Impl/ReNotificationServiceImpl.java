package com.emergency.rollcall.service.Impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.emergency.rollcall.dao.RenotificationDao;
import com.emergency.rollcall.dto.ReNotificationDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.Renotification;
import com.emergency.rollcall.service.ReNotificationService;

@Service
public class ReNotificationServiceImpl implements ReNotificationService {
	
	private final Logger logger = Logger.getLogger(ReNotificationService.class.getName());

	@Autowired
	private RenotificationDao renotificationDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveReNotification(ReNotificationDto notiDto) {
		ResponseDto res = new ResponseDto();
		Renotification renotification = new Renotification();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		renotification = modelMapper.map(notiDto, Renotification.class);
		logger.info("Saving re-notification entity: " + notiDto);

		renotification.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
		try {
			if (renotification.getSyskey() == 0) {
				Renotification entityres = renotificationDao.save(renotification);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
					logger.info("Successfully saving notification entity: " + entityres);
				}
			} else {
				Renotification entityres = renotificationDao.save(renotification);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
					logger.info("Successfully updating notification entity: " + entityres);
				}
			}
		} catch (DataAccessException e) {
			logger.info("Error notification entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error notification entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ReNotificationDto getById(long id) {
		ReNotificationDto conditionDto = new ReNotificationDto();
		Renotification ReNotification = new Renotification();
		logger.info("Retrieving notification entity: " + id);
		try {
			Optional<Renotification> notificationOptional = renotificationDao.findById(id);
			if (notificationOptional.isPresent()) {
				ReNotification = notificationOptional.get();
				conditionDto = modelMapper.map(ReNotification, ReNotificationDto.class);
				logger.info("Successfully retrieving notification entity: " + conditionDto);
			}
		} catch (DataAccessException dae) {
			logger.info("Error re-notification entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error retrieving re-notificatioon enitity " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return conditionDto;
	}

	@Override
	public ResponseDto updateReNotification(ReNotificationDto notiDto) {
		ResponseDto res = new ResponseDto();
		Renotification renotification = new Renotification();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		logger.info("Updating notification entity: " + notiDto);
		try {
			Optional<Renotification> notiOptional = renotificationDao.findById(notiDto.getSyskey());
			if (notiOptional.isPresent()) {
				renotification = notiOptional.get();
				createdDate = renotification.getCreateddate();
				renotification = modelMapper.map(notiDto, Renotification.class);
				renotification.setCreateddate(createdDate);
				renotification.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				renotificationDao.save(renotification);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Succesfully updating notification entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Data does not found to upate notification entity: " + res.getMessage());
			}
		} catch (DataAccessException e) {
			logger.info("Error updating notification entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error updating notification entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}
		return res;
	}

	@Override
	public ResponseDto deleteReNotification(long id) {
		ResponseDto res = new ResponseDto();
		Renotification renotification = new Renotification();
		logger.info("Deleting notification entity: " + id);
		try {
			Optional<Renotification> renotiOptional = renotificationDao.findById(id);
			if (renotiOptional.isPresent()) {
				renotification = renotiOptional.get();
				renotificationDao.delete(renotification);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Successfully deleting notification entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("Data does not found to delete notification entity: " + res.getMessage());
			}
		} catch (DataAccessException e) {
			logger.info("Error deleting notification entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error deleting notification entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public Page<ReNotificationDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<Renotification> notiList;
		List<ReNotificationDto> notiDtoList = new ArrayList<>();
		logger.info("Searching notification entity: " );
		try {
			if (params == null || params.isEmpty()) {
				notiList = renotificationDao.findByNameOrCode(pageRequest);	
			} else {
				notiList = renotificationDao.findByNameOrCode(pageRequest, params);	
			}
			if (notiList != null) {
				for (Renotification renotification : notiList) {
					ReNotificationDto notiDto = new ReNotificationDto();
					notiDto = modelMapper.map(renotification, ReNotificationDto.class);
					notiDtoList.add(notiDto);
				}
				logger.info("Successfully searching notification entity: " + notiDtoList );
			}

		} catch (DataAccessException dae) {
			logger.info("Error searcing notification entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching notification entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
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
