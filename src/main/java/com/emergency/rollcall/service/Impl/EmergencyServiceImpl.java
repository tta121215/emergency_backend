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

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		emergency.setCreateddate(this.yyyyMMddFormat(strCreatedDate));

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

		return res;
	}

	@Override
	public EmergencyDto getById(long id) {
		EmergencyDto emergencyDto = new EmergencyDto();
		Emergency emergency = new Emergency();
		Optional<Emergency> emergencyOptional = emergencyDao.findById(id);
		if (emergencyOptional.isPresent()) {
			emergency = emergencyOptional.get();
			emergencyDto = modelMapper.map(emergency, EmergencyDto.class);
		}
		return emergencyDto;
	}

	@Override
	public ResponseDto updateEmergency(EmergencyDto emergencyDto) {
		ResponseDto res = new ResponseDto();
		Emergency emergency = new Emergency();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		Optional<Emergency> emergencyOptional = emergencyDao.findById(emergencyDto.getSyskey());
		if (emergencyOptional.isPresent()) {
			emergency = emergencyOptional.get();
			emergency = modelMapper.map(emergencyDto, Emergency.class);
			emergency.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
			emergencyDao.save(emergency);
			res.setStatus_code(200);
			res.setMessage("Successfully Updated");
		} else {
			res.setMessage("Data does not found");
		}

		return res;
	}

	@Override
	public ResponseDto deleteEmergency(long id) {
		ResponseDto res = new ResponseDto();
		Emergency emergency = new Emergency();

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

		return res;
	}

	@Override
	public Page<EmergencyDto> searchByParams(int page, int size, String params) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Emergency> emergencyList;
		List<EmergencyDto> emergencyDtoList = new ArrayList<>();
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

		return new PageImpl<>(emergencyDtoList, pageRequest, emergencyList.getTotalElements());
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
