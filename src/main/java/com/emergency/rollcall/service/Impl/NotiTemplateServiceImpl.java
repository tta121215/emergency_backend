package com.emergency.rollcall.service.Impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.emergency.rollcall.dao.NotiTemplateDao;
import com.emergency.rollcall.dto.NotiTemplateDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.NotiTemplate;
import com.emergency.rollcall.service.NotiTemplateService;

@Service
public class NotiTemplateServiceImpl implements NotiTemplateService {

	private final Logger logger = Logger.getLogger(NotiTemplateService.class.getName());

	@Autowired
	private NotiTemplateDao notiTemplateDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveNotiTemplate(NotiTemplateDto notiDto) {
		ResponseDto res = new ResponseDto();
		NotiTemplate notiTemplate = new NotiTemplate();
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		logger.info("Saving notification entity: " + notiDto);
		try {
			notiTemplate = modelMapper.map(notiDto, NotiTemplate.class);

			notiTemplate.setCreateddate(this.yyyyMMddFormat(strCreatedDate));

			if (notiTemplate.getSyskey() == 0) {
				NotiTemplate entityres = notiTemplateDao.save(notiTemplate);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
					logger.info("Successsfully saving notification entity: " + entityres);
				}
			} else {
				NotiTemplate entityres = notiTemplateDao.save(notiTemplate);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
					logger.info("Successsfully updating notification entity: " + entityres);
				}
			}

		} catch (DataAccessException e) {
			logger.info("Error saving notification entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Successsfully saving notification entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public NotiTemplateDto getById() {
		NotiTemplateDto notificationDto = new NotiTemplateDto();
		logger.info("Successsfully retrieving notification entity: ");
		try {
			List<NotiTemplate> notificationList = notiTemplateDao.findAll();
			if (notificationList.size() > 0) {
				NotiTemplate notiTemplate = notificationList.get(0);
				notificationDto = modelMapper.map(notiTemplate, NotiTemplateDto.class);
				logger.info("Successsfully retrieving notification entity: " + notificationDto);
			}

		} catch (DataAccessException dae) {
			logger.info("Error retrieving notification entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error retrieving notification entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}
		return notificationDto;
	}

	@Override
	public ResponseDto updateNotiTemplate(NotiTemplateDto notiDto) {
		ResponseDto res = new ResponseDto();
		NotiTemplate notification = new NotiTemplate();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		logger.info("Updating notification entity: " + notiDto);
		try {
			Optional<NotiTemplate> notiOptional = notiTemplateDao.findById(notiDto.getSyskey());
			if (notiOptional.isPresent()) {
				notification = notiOptional.get();
				createdDate = notification.getCreateddate();
				notification = modelMapper.map(notiDto, NotiTemplate.class);
				notification.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				notification.setCreateddate(createdDate);

				notiTemplateDao.save(notification);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Successsfully updating notification entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Data does not found retrieving notification entity: " + res.getMessage());
			}

		} catch (DataAccessException e) {
			logger.info("Error retrieving notification entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error retrieving notification entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ResponseDto deleteNotiTemplate(long id) {
		ResponseDto res = new ResponseDto();
		NotiTemplate notification = new NotiTemplate();
		logger.info("Deleting notification entity: " + id);
		try {
			Optional<NotiTemplate> notiOptional = notiTemplateDao.findById(id);
			if (notiOptional.isPresent()) {
				notification = notiOptional.get();
				notiTemplateDao.delete(notification);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Successsfully deleted notification entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("No data found deleting notification entity: " + res.getMessage());
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
