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
import com.emergency.rollcall.dao.LocEmergencyDao;

import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.LocEmergencyDto;
import com.emergency.rollcall.entity.LocEmergency;
import com.emergency.rollcall.service.LocEmergencyService;

@Service
public class LocEmergencyServiceImpl implements LocEmergencyService {

	@Autowired
	private LocEmergencyDao locEmergencyDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveLocEmergency(LocEmergencyDto locEmergencyDto) {
		ResponseDto res = new ResponseDto();
		LocEmergency locEmergency = new LocEmergency();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		locEmergency = modelMapper.map(locEmergencyDto, LocEmergency.class);

		locEmergency.setCreateddate(this.yyyyMMddFormat(strCreatedDate));

		if (locEmergency.getSyskey() == 0) {
			LocEmergency entityres = locEmergencyDao.save(locEmergency);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Saved");
			}
		} else {
			LocEmergency entityres = locEmergencyDao.save(locEmergency);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
			}
		}

		return res;
	}

	@Override
	public LocEmergencyDto getById(long id) {
		LocEmergencyDto locEmergencyDto = new LocEmergencyDto();
		LocEmergency locEmergency = new LocEmergency();
		Optional<LocEmergency> LocEmergencyOptional = locEmergencyDao.findById(id);
		if (LocEmergencyOptional.isPresent()) {
			locEmergency = LocEmergencyOptional.get();
			locEmergencyDto = modelMapper.map(locEmergency, LocEmergencyDto.class);
		}
		return locEmergencyDto;
	}

	@Override
	public ResponseDto updateLocEmergency(LocEmergencyDto locEmergencyDto) {
		ResponseDto res = new ResponseDto();
		LocEmergency locEmergency = new LocEmergency();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		Optional<LocEmergency> LocEmergencyOptional = locEmergencyDao.findById(locEmergencyDto.getSyskey());
		if (LocEmergencyOptional.isPresent()) {
			locEmergency = LocEmergencyOptional.get();
			locEmergency = modelMapper.map(locEmergencyDto, LocEmergency.class);
			locEmergency.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
			locEmergencyDao.save(locEmergency);
			res.setStatus_code(200);
			res.setMessage("Successfully Updated");
		} else {
			res.setMessage("Data does not found");
		}

		return res;
	}

	@Override
	public ResponseDto deleteLocEmergency(long id) {
		ResponseDto res = new ResponseDto();
		LocEmergency locEmergency = new LocEmergency();

		Optional<LocEmergency> LocEmergencyOptional = locEmergencyDao.findById(id);
		if (LocEmergencyOptional.isPresent()) {
			locEmergency = LocEmergencyOptional.get();
			locEmergencyDao.delete(locEmergency);
			res.setStatus_code(200);
			res.setMessage("Successfully Deleted");
		} else {
			res.setStatus_code(401);
			res.setMessage("No data found");
		}

		return res;
	}

	@Override
	public Page<LocEmergencyDto> searchByParams(int page, int size, String params) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<LocEmergency> locEmergencyList;
		List<LocEmergencyDto> locEmergencyDtoList = new ArrayList<>();
		if (params == null || params.isEmpty()) {
			locEmergencyList = locEmergencyDao.findByNameOrCode(pageRequest);
			if (locEmergencyList != null) {
				for (LocEmergency locEmergency : locEmergencyList) {
					LocEmergencyDto locEmergencyDto = new LocEmergencyDto();
					locEmergencyDto = modelMapper.map(locEmergency, LocEmergencyDto.class);
					locEmergencyDtoList.add(locEmergencyDto);
				}
			}

		} else {
			locEmergencyList = locEmergencyDao.findByNameOrCode(pageRequest, params);
			if (locEmergencyList != null) {
				for (LocEmergency locEmergency : locEmergencyList) {
					LocEmergencyDto locEmergencyDto = new LocEmergencyDto();
					locEmergencyDto = modelMapper.map(locEmergency, LocEmergencyDto.class);
					locEmergencyDtoList.add(locEmergencyDto);
				}
			}
		}

		return new PageImpl<>(locEmergencyDtoList, pageRequest, locEmergencyList.getTotalElements());
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
