package com.emergency.rollcall.service.Impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.emergency.rollcall.dao.EmergencyDao;
import com.emergency.rollcall.dto.EmergencyDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.Emergency;
import com.emergency.rollcall.service.EmergencyService;

@Service
public class EmergencyServiceImpl implements EmergencyService {

	@Autowired
	private EmergencyDao emergencyDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveEmergency(EmergencyDto emergencyDto) {
		ResponseDto res = new ResponseDto();
		Emergency emergency = new Emergency();
		emergency = modelMapper.map(emergencyDto, Emergency.class);

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		emergency.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
		try {
			if (emergency.getSyskey() == 0) {
				Emergency entityres = emergencyDao.save(emergency);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
				}
			} else {
				Emergency entityres = emergencyDao.save(emergency);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
				}
			}
		} catch (DataAccessException e) {
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public EmergencyDto getById(long id) {
		EmergencyDto emergencyDto = new EmergencyDto();
		Emergency emergency = new Emergency();
		try {
			Optional<Emergency> emergencyOptional = emergencyDao.findById(id);
			if (emergencyOptional.isPresent()) {
				emergency = emergencyOptional.get();
				emergencyDto = modelMapper.map(emergency, EmergencyDto.class);
			}
		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return emergencyDto;
	}

	@Override
	public ResponseDto updateEmergency(EmergencyDto emergencyDto) {
		ResponseDto res = new ResponseDto();
		Emergency emergency = new Emergency();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		try {
			Optional<Emergency> emergencyOptional = emergencyDao.findById(emergencyDto.getSyskey());
			if (emergencyOptional.isPresent()) {
				emergency = emergencyOptional.get();
				createdDate = emergency.getCreateddate();
				emergency = modelMapper.map(emergencyDto, Emergency.class);
				emergency.setCreateddate(createdDate);
				emergency.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				emergencyDao.save(emergency);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
			}

		} catch (DataAccessException e) {
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ResponseDto deleteEmergency(long id) {
		ResponseDto res = new ResponseDto();
		Emergency emergency = new Emergency();
		try {
			Optional<Emergency> emergencyOptional = emergencyDao.findById(id);
			if (emergencyOptional.isPresent()) {
				emergency = emergencyOptional.get();
				emergencyDao.delete(emergency);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
			}
		} catch (DataAccessException e) {
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public Page<EmergencyDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<Emergency> emergencyList;
		List<EmergencyDto> emergencyDtoList = new ArrayList<>();
		try {
			if (params == null || params.isEmpty()) {
				emergencyList = emergencyDao.findByNameOrCode(pageRequest);
				if (emergencyList != null) {
					for (Emergency emergency : emergencyList) {
						EmergencyDto emergencyDto = new EmergencyDto();
						emergencyDto = modelMapper.map(emergency, EmergencyDto.class);
						emergencyDtoList.add(emergencyDto);
					}
				}

			} else {
				emergencyList = emergencyDao.findByNameOrCode(pageRequest, params);
				if (emergencyList != null) {
					for (Emergency emergency : emergencyList) {
						EmergencyDto emergencyDto = new EmergencyDto();
						emergencyDto = modelMapper.map(emergency, EmergencyDto.class);
						emergencyDtoList.add(emergencyDto);
					}
				}
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}
		return new PageImpl<>(emergencyDtoList, pageRequest, emergencyList.getTotalElements());
	}

	@Override
	public List<EmergencyDto> getAllList() {

		List<EmergencyDto> emergencyDtoList = new ArrayList<>();
		List<Emergency> emergencyList = new ArrayList<>();
		try {
			emergencyList = emergencyDao.findAllByStatus(1);
			if (emergencyList != null) {
				for (Emergency emergency : emergencyList) {
					EmergencyDto emergencyDto = new EmergencyDto();
					emergencyDto = modelMapper.map(emergency, EmergencyDto.class);
					emergencyDtoList.add(emergencyDto);
				}
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return emergencyDtoList;
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
