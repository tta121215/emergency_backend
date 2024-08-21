package com.emergency.rollcall.service.Impl;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String strCreatedDate = dateTime.format(formatter);
		LocalDateTime startTime = LocalDateTime.now();

		notiLog = modelMapper.map(notiReadLogDto, NotiReadLog.class);
		logger.info("Saving notiRead entity: " + notiReadLogDto);
		notiLog.setCreatedDate(this.yyyyMMddFormat(strCreatedDate));
		notiLog.setCreatedTime(startTime.format(timeFormatter));
		notiLog.setReadNotiDate(strCreatedDate);
		notiLog.setReadNotiTime(startTime.format(timeFormatter));	
		
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
		Sort sort = Sort.by(sortDirection, sortBy);
		if (sortBy.equals("name")) {
			sort = Sort.by(sortDirection, "u.name");
		} else if (sortBy.equals("staffid")) {
			sort = Sort.by(sortDirection, "u.staffid");
		} else if (sortBy.equals("ename")) {
			sort = Sort.by(sortDirection, "ec.name");
		} else {
			sort = Sort.by(sortDirection, "u.name");
		}
		PageRequest pageRequest = PageRequest.of(page, size, sort);			
		List<NotiReadLogDto> readNotiList = new ArrayList<>();
		Page<Map<String, Object>> readNotiPage = null;
		logger.info("Searching noti read log entity: ");
		try {
			if (params == null || params.isEmpty()) {
				readNotiPage = notiReadLogDao.findByUserName(pageRequest);
			} else {
				readNotiPage = notiReadLogDao.findByUserName(pageRequest,params);
			}
			if (!readNotiPage.isEmpty()) {
				readNotiList = readNotiPage.stream().map(readNoti -> {
					NotiReadLogDto notiReadDto = new NotiReadLogDto();					
					notiReadDto.setUserName((String) readNoti.get("username"));
					notiReadDto.setEmergencyName((String) readNoti.get("ename"));
					notiReadDto.setStaffId((String) readNoti.get("staffid"));
					notiReadDto.setReadNotiDate((String) readNoti.get("notidate"));
					notiReadDto.setReadNotiTiime((String) readNoti.get("notitime"));
					return notiReadDto;
				}).collect(Collectors.toList());
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
		

		return new PageImpl<>(readNotiList, pageRequest, readNotiPage.getTotalElements());
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
