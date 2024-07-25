package com.emergency.rollcall.service.Impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.emergency.rollcall.dao.AssemblyCheckInDao;
import com.emergency.rollcall.dto.AssemblyCheckInDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.AssemblyCheckIn;
import com.emergency.rollcall.service.AssemblyCheckInService;

@Service
public class AssemblyCheckInServiceImpl implements AssemblyCheckInService {

	private final Logger logger = Logger.getLogger(AssemblyCheckInService.class.getName());

	@Autowired
	private AssemblyCheckInDao assemblyCheckInDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveAssemblyCheckIn(AssemblyCheckInDto data) {
		ResponseDto res = new ResponseDto();
		AssemblyCheckIn entity = new AssemblyCheckIn();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String strCreatedDate = dateTime.format(formatter);
		String strCurrentTime = dateTime.format(timeFormatter);
		entity = modelMapper.map(data, AssemblyCheckIn.class);
		entity.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
		System.out.println("current date = " + strCreatedDate);
		entity.setCurrentdate(strCreatedDate);
		entity.setCurrenttime(strCurrentTime);
		logger.info("Saving assembly check in entity: " + entity);

		try {
			if (entity.getSyskey() == 0) {
				AssemblyCheckIn entityres = assemblyCheckInDao.save(entity);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved.");
					logger.info("Assembly check in entity saved successfully: " + entityres);
				}
			} else {
				AssemblyCheckIn entityres = assemblyCheckInDao.save(entity);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated.");
					logger.info("Assembly check in entity updated successfully: " + entityres);
				}
			}
		} catch (DataAccessException e) {
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
			logger.severe("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
			logger.severe("Database error occurred: " + e.getMessage());
		}
		return res;
	}

	@Override
	public List<AssemblyCheckInDto> getAllListByActivationId(Long id) {
		List<AssemblyCheckInDto> assemblyCheckInDtoList = new ArrayList<>();
		List<AssemblyCheckIn> assemblyCheckInList = new ArrayList<>();
		try {
			assemblyCheckInList = assemblyCheckInDao.getAllListByActivationId(id);
			if (assemblyCheckInList != null) {
				for (AssemblyCheckIn assemblyCheckIn : assemblyCheckInList) {
					AssemblyCheckInDto assemblyCheckInDto = new AssemblyCheckInDto();
					assemblyCheckInDto = modelMapper.map(assemblyCheckIn, AssemblyCheckInDto.class);
					assemblyCheckInDtoList.add(assemblyCheckInDto);
				}
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return assemblyCheckInDtoList;
	}

	public String yyyyMMddFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null) {
			l_Date = aDate.replaceAll("-", "");
		}
		return l_Date;
	}
}
