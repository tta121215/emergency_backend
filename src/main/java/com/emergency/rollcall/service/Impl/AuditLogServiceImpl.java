package com.emergency.rollcall.service.Impl;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.emergency.rollcall.dao.AuditLogDao;


import com.emergency.rollcall.dto.AuditLogDto;

import com.emergency.rollcall.dto.ResponseDto;

import com.emergency.rollcall.entity.AuditLog;

import com.emergency.rollcall.service.AuditLogService;



@Service
public class AuditLogServiceImpl implements AuditLogService {

	private final Logger logger = Logger.getLogger(AuditLogService.class.getName());

	@Autowired
	private AuditLogDao auditLogDao;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public void saveAuditLog(String username, String apiMethod, String details, String ipaddress,
			String browserVersion) {
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		LocalTime strCreatedTime = dateTime.toLocalTime();
		
		AuditLog auditLog = new AuditLog();
        auditLog.setCreatedDate(strCreatedDate);
        auditLog.setCreatedTime(strCreatedTime.toString());
        auditLog.setUser(username);
        auditLog.setApiMethod(apiMethod);
        auditLog.setDetails(details);
        auditLog.setBrowserVersion(browserVersion);
        auditLogDao.save(auditLog);
	}
	

	@Override
	public Page<AuditLogDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		// TODO Auto-generated method stub
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<AuditLog> auditLogList;
		List<AuditLogDto> auditLogDtoList = new ArrayList<>();
		
		logger.info("Searching audit log entity: ");
		try {
			if (params == null || params.isEmpty()) {
				auditLogList = auditLogDao.findByUserName(pageRequest);
			} else {
				auditLogList = auditLogDao.findByUserName(pageRequest, params);
			}
			if (auditLogList != null) {
				for (AuditLog auditLog : auditLogList) {
					AuditLogDto auditLogDto = new AuditLogDto();
					auditLogDto = modelMapper.map(auditLog, AuditLogDto.class);
					auditLogDtoList.add(auditLogDto);
				}
				logger.info("Successfully searching audit log entity: " + auditLogDtoList);
			}
		} catch (DataAccessException dae) {
			logger.info("Error searching role entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching role entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(auditLogDtoList, pageRequest, auditLogList.getTotalElements());
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
