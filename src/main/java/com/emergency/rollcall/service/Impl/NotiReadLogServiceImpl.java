package com.emergency.rollcall.service.Impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.emergency.rollcall.dao.NotiReadLogDao;
import com.emergency.rollcall.dto.NotiReadLogDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.NotiReadLog;
import com.emergency.rollcall.service.AuditLogService;
import com.emergency.rollcall.service.NotiReadLogService;

@Service
public class NotiReadLogServiceImpl implements NotiReadLogService {

	private final Logger logger = Logger.getLogger(AuditLogService.class.getName());

	@Autowired
	private NotiReadLogDao notiReadLogDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveNotiReadLog(NotiReadLogDto notiReadLogDto) {
		ResponseDto res = new ResponseDto();
		NotiReadLog notiLog = new NotiReadLog();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		notiLog = modelMapper.map(notiReadLogDto, NotiReadLog.class);
		logger.info("Saving notiRead entity: " + notiReadLogDto);
		notiLog.setCreatedDate(this.yyyyMMddFormat(strCreatedDate));
		try {
			if (notiLog.getSyskey() == 0) {
				NotiReadLog entityres = notiReadLogDao.save(notiLog);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
					logger.info("Successfully save condition : " + entityres);
				}
			} else {
				NotiReadLog entityres = notiReadLogDao.save(notiLog);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
					logger.info("Successfully update condition entity: " + entityres);
				}
			}
		} catch (DataAccessException e) {
			logger.info("Error saving condition entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error saving condition entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;

	}

	@Override
	public Page<NotiReadLogDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		// TODO Auto-generated method stub
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<NotiReadLog> auditLogList;
		List<NotiReadLogDto> auditLogDtoList = new ArrayList<>();

		logger.info("Searching audit log entity: ");
		try {
			if (params == null || params.isEmpty()) {
				auditLogList = notiReadLogDao.findByUserName(pageRequest);
			} else {
				auditLogList = notiReadLogDao.findByUserName(pageRequest, params);
			}
			if (auditLogList != null) {
				for (NotiReadLog auditLog : auditLogList) {
					NotiReadLogDto auditLogDto = new NotiReadLogDto();
					auditLogDto = modelMapper.map(auditLog, NotiReadLogDto.class);
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
