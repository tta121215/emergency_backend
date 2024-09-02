package com.emergency.rollcall.service.Impl;

import java.time.ZoneId;
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

import com.emergency.rollcall.dao.EmergencyDao;
import com.emergency.rollcall.dto.EmergencyDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.Emergency;
import com.emergency.rollcall.service.EmergencyService;

@Service
public class EmergencyServiceImpl implements EmergencyService {
	
	private final Logger logger = Logger.getLogger(EmergencyService.class.getName());

	@Autowired
	private EmergencyDao emergencyDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveEmergency(EmergencyDto emergencyDto) {
		ResponseDto res = new ResponseDto();
		Emergency emergency = new Emergency();
		emergency = modelMapper.map(emergencyDto, Emergency.class);

		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String strCreatedDate = malaysiaDateTime.format(timeformatter);

		emergency.setCreateddate(strCreatedDate);
		
		if(emergency.getIsDefault() == 1) {
			emergencyDao.updateEmergencyDefault(0);
			logger.info("Succesfully updating Emergency entity: " + "update default emergency to not default.");
		}
		
		try {
			if (emergency.getSyskey() == 0) {
				Emergency entityres = emergencyDao.save(emergency);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
					logger.info("Successfully saving Emergency entity: " + entityres);
				}
			} else {
				Emergency entityres = emergencyDao.save(emergency);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
					logger.info("Successfully updating Emergency entity: " + entityres);
				}
			}
		} catch (DataAccessException e) {
			logger.info("Error saving Emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error saving Emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public EmergencyDto getById(long id) {
		EmergencyDto emergencyDto = new EmergencyDto();
		Emergency emergency = new Emergency();
		logger.info("Searching Emergency entity: " + id);
		try {
			Optional<Emergency> emergencyOptional = emergencyDao.findById(id);
			if (emergencyOptional.isPresent()) {
				emergency = emergencyOptional.get();
				emergencyDto = modelMapper.map(emergency, EmergencyDto.class);
				logger.info("Successfully retrieving Emergency entity: " + emergencyDto);
			}
		} catch (DataAccessException dae) {
			logger.info("Error retrieving Emergency entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error retrieving Emergency entity: " + e.getMessage());
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
		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String strCreatedDate = malaysiaDateTime.format(timeformatter);
		logger.info("Updating Emergency entity: " + emergencyDto);
		try {
			Optional<Emergency> emergencyOptional = emergencyDao.findById(emergencyDto.getSyskey());
			if (emergencyOptional.isPresent()) {				
				emergency = emergencyOptional.get();
				createdDate = emergency.getCreateddate();
				emergency = modelMapper.map(emergencyDto, Emergency.class);
				emergency.setCreateddate(createdDate);
				emergency.setModifieddate(strCreatedDate);		
				
				if(emergency.getIsDefault() == 1) {
					emergencyDao.updateEmergencyDefault(0);
				}
				
				emergencyDao.save(emergency);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Successfully updating Emergency entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Data does not found updating Emergency entity: " + res.getMessage());
			}

		} catch (DataAccessException e) {
			logger.info("Error updating Emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error updating Emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ResponseDto deleteEmergency(long id) {
		ResponseDto res = new ResponseDto();
		Emergency emergency = new Emergency();
		logger.info("Deleteing Emergency entity: " + id);
		try {
			Long count = emergencyDao.countEmergencyActivatesByEmergencyTypeId(id);

			if (count > 0) {
				res.setStatus_code(200);
				res.setMessage("Cannot delete the emergency type because it is associated with active emergencies.");
				return res;
			}
			
			Optional<Emergency> emergencyOptional = emergencyDao.findById(id);
			if (emergencyOptional.isPresent()) {
				emergency = emergencyOptional.get();
				emergency.setIsDelete(1);
				emergencyDao.save(emergency);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Succesfully deleting Emergency entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("No data found Emergency entity: " + res.getMessage());
			}
		} catch (DataAccessException e) {
			logger.info("Error deleteing Emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error deleting Emergency entity: " + e.getMessage());
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
		logger.info("Searcing Emergency entity: ");
		try {
			if (params == null || params.isEmpty()) {
				emergencyList = emergencyDao.findByNameOrCode(pageRequest);
			} else {
				emergencyList = emergencyDao.findByNameOrCode(pageRequest, params);
			}
			if (emergencyList != null) {
				for (Emergency emergency : emergencyList) {
					EmergencyDto emergencyDto = new EmergencyDto();
					emergencyDto = modelMapper.map(emergency, EmergencyDto.class);
					emergencyDtoList.add(emergencyDto);
				}
				logger.info("Successfully searching Emergency entity: " + emergencyDtoList);
			}

		} catch (DataAccessException dae) {
			logger.info("Error searching Emergency entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching Emergency entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}
		return new PageImpl<>(emergencyDtoList, pageRequest, emergencyList.getTotalElements());
	}

	@Override
	public List<EmergencyDto> getAllList() {

		List<EmergencyDto> emergencyDtoList = new ArrayList<>();
		List<Emergency> emergencyList = new ArrayList<>();
		logger.info("Retrieving Emergency entity: " );
		try {
			emergencyList = emergencyDao.findAllByStatusAndIsDelete(1, 0);
			if (emergencyList != null) {
				for (Emergency emergency : emergencyList) {
					EmergencyDto emergencyDto = new EmergencyDto();
					emergencyDto = modelMapper.map(emergency, EmergencyDto.class);
					emergencyDtoList.add(emergencyDto);
				}
				logger.info("Successfully retrieving all Emergency entity: " + emergencyDtoList);
			}

		} catch (DataAccessException dae) {
			logger.info("Error retrieving Emergency entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error retrieving Emergency entity: " + e.getMessage());
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
