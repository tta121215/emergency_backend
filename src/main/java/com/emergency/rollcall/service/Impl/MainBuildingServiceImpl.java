package com.emergency.rollcall.service.Impl;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.emergency.rollcall.dao.LocEmergencyDao;
import com.emergency.rollcall.dao.MainBuildingDao;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.RouteDto;
import com.emergency.rollcall.dto.MainBuildingDto;
import com.emergency.rollcall.dto.AssemblyDto;
import com.emergency.rollcall.dto.LocEmergencyDto;
import com.emergency.rollcall.dto.LocationDto;
import com.emergency.rollcall.entity.Assembly;
import com.emergency.rollcall.entity.LocEmergency;
import com.emergency.rollcall.entity.MainBuilding;
import com.emergency.rollcall.entity.Route;
import com.emergency.rollcall.service.MainBuildingService;


@Service
public class MainBuildingServiceImpl implements MainBuildingService{

	private final Logger logger = Logger.getLogger(MainBuildingService.class.getName());

	@Autowired
	private MainBuildingDao mainBuildingDao;
	
	@Autowired
	private LocEmergencyDao locEmerDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveMainBuilding(MainBuildingDto mainBuildingDto) {
		ResponseDto res = new ResponseDto();
		MainBuilding mainBuilding = new MainBuilding();
		List<LocEmergency> locEmerList = new ArrayList<>();

		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String strCreatedDate = malaysiaDateTime.format(timeformatter);
		
		mainBuilding = modelMapper.map(mainBuildingDto, MainBuilding.class);
		mainBuilding.setCreateddate(strCreatedDate);
		
		logger.info("Saving main building entity: " + mainBuildingDto);
		try {	
			
			if (mainBuildingDto.getLocEmergencyList() != null) {
				for (LocEmergencyDto locEmergencyData : mainBuildingDto.getLocEmergencyList()) {
					Optional<LocEmergency> locEmergencyOptional = locEmerDao.findById(locEmergencyData.getSyskey());
					if (locEmergencyOptional.isPresent() && locEmergencyOptional.get().getSyskey() != 0) {
						locEmerList.add(locEmergencyOptional.get());
					} else {
						res.setStatus_code(401);
						res.setMessage("Route data is invalid.");
						return res;
					}
				}
				mainBuilding.setLocEmergencyList(locEmerList);				
			}
			if (mainBuilding.getSyskey() == 0) {
				MainBuilding entityres = mainBuildingDao.save(mainBuilding);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
					logger.info("Successfully saving main building entity: " + entityres);
				}
			} else {
				MainBuilding entityres = mainBuildingDao.save(mainBuilding);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
				}
			}
		} catch (DataAccessException e) {
			logger.info("Error saving main building entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error saving main building entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public MainBuildingDto getById(long id) {
		MainBuildingDto mainBuildingDto = new MainBuildingDto();
		MainBuilding mainBuilding = new MainBuilding();
		logger.info("Retrieving main building entity: " + id);
		try {
			Optional<MainBuilding> MainBuildingOptional = mainBuildingDao.findById(id);
			if (MainBuildingOptional.isPresent()) {
				mainBuilding = MainBuildingOptional.get();
				mainBuildingDto = modelMapper.map(mainBuilding, MainBuildingDto.class);
				logger.info("Successfully retrived main building entity: " + mainBuildingDto);
			}

		} catch (DataAccessException dae) {
			logger.info("Error retrieving main building entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error main building entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return mainBuildingDto;
	}

	@Override
	public ResponseDto updateMainBuilding(MainBuildingDto mainBuildingDto) {
		ResponseDto res = new ResponseDto();
		MainBuilding mainBuilding = new MainBuilding();
		List<LocEmergency> locEmerList = new ArrayList<>();
		
		String createdDate;
		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String strCreatedDate = malaysiaDateTime.format(timeformatter);
		logger.info("Updating main building entity: " + mainBuildingDto);
		try {
			Optional<MainBuilding> MainBuildingOptional = mainBuildingDao.findById(mainBuildingDto.getSyskey());
			if (MainBuildingOptional.isPresent()) {
				mainBuilding = MainBuildingOptional.get();
				createdDate = mainBuilding.getCreateddate();
				mainBuilding = modelMapper.map(mainBuildingDto, MainBuilding.class);
				mainBuilding.setCreateddate(createdDate);
				mainBuilding.setModifieddate(strCreatedDate);	
				
				if (mainBuildingDto != null) {
					if(mainBuildingDto.getLocEmergencyList() != null) {
						for (LocEmergencyDto locEmerData : mainBuildingDto.getLocEmergencyList()) {
							Optional<LocEmergency> locEmerOptional = locEmerDao.findById(locEmerData.getSyskey());
							if (locEmerOptional.isPresent() && locEmerOptional.get().getSyskey() != 0) {
								locEmerList.add(locEmerOptional.get());
							} else {
								res.setStatus_code(401);
								res.setMessage("Loc Emergency data is invalid.");
								return res;
							}
						}
						mainBuilding.setLocEmergencyList(locEmerList);
					}					
				}

				mainBuildingDao.save(mainBuilding);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Successfully updating main building entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Data does not found main building entity: " + res.getMessage());
			}

		} catch (DataAccessException e) {
			logger.info("Error updating main building entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error updating main building entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ResponseDto deleteMainBuilding(long id) {
		ResponseDto res = new ResponseDto();
		MainBuilding mainBuilding = new MainBuilding();
		logger.info("Deleting main building entity: " + id);
		try {
			//Long count = mainBuildingDao.countEmergencyActivatesByEmergencyLocationId(id);

//			if (count > 0) {
//				res.setStatus_code(200);
//				res.setMessage("Cannot delete the emergency location because it is associated with active emergencies.");
//				return res;
//			}
			
			Optional<MainBuilding> MainBuildingOptional = mainBuildingDao.findById(id);
			if (MainBuildingOptional.isPresent()) {
				mainBuilding = MainBuildingOptional.get();
				mainBuilding.setIsDelete(1);
				mainBuildingDao.save(mainBuilding);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Successfuly deleted emergency location data : " + res);
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("Does not found deleted emergency location data : " + res);
			}
		} catch (DataAccessException e) {
			logger.info("Error deleting main building entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error deleting main building entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public Page<MainBuildingDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<MainBuilding> mainBuildingList;
		List<MainBuildingDto> mainBuildingDtoList = new ArrayList<>();	
		List<LocEmergencyDto> locEmerDtoList = new ArrayList<>();
		LocEmergencyDto locEmerDto = new LocEmergencyDto();
		logger.info("Searching main building entity: ");
		try {
			if (params == null || params.isEmpty()) {
				mainBuildingList = mainBuildingDao.findByNameOrCode(pageRequest);
			} else {
				mainBuildingList = mainBuildingDao.findByNameOrCode(pageRequest, params);
			}
			if (mainBuildingList != null) {
				for (MainBuilding mainBuilding : mainBuildingList) {
					MainBuildingDto mainBuildingDto = new MainBuildingDto();
					mainBuildingDto = modelMapper.map(mainBuilding, MainBuildingDto.class);					
					mainBuildingDtoList.add(mainBuildingDto);
					
					if (mainBuilding.getLocEmergencyList() != null) {
						for (LocEmergency locEmer : mainBuilding.getLocEmergencyList()) {
							locEmerDto = new LocEmergencyDto();
							locEmerDto = modelMapper.map(locEmer, LocEmergencyDto.class);
							locEmerDtoList.add(locEmerDto);
						}
						mainBuildingDto.setLocEmergencyList(locEmerDtoList);
						locEmerDtoList = new ArrayList<>();
					}
					logger.info("Successfully searching main building entity: " + mainBuildingDtoList);
				}
			}
		} catch (DataAccessException dae) {
			logger.info("Error retrieving main building entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error retrieving main building entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}
		return new PageImpl<>(mainBuildingDtoList, pageRequest, mainBuildingList.getTotalElements());
	}

	@Override
	public List<MainBuildingDto> getAllList() {

		List<MainBuildingDto> mainBuildingDtoList = new ArrayList<>();
		List<MainBuilding> mainBuildingList = new ArrayList<>();
		LocEmergencyDto locEmerDto = new LocEmergencyDto();
		List<LocEmergencyDto> locEmerDtoList = new ArrayList<>();
		try {
			mainBuildingList = mainBuildingDao.findAllByStatusAndIsDelete(1, 0);
			if (mainBuildingList != null) {
				for (MainBuilding mainBuilding : mainBuildingList) {
					MainBuildingDto mainBuildingDto = new MainBuildingDto();
					mainBuildingDto = modelMapper.map(mainBuilding, MainBuildingDto.class);
					mainBuildingDtoList.add(mainBuildingDto);
					if (mainBuilding.getLocEmergencyList() != null) {
						for (LocEmergency locEmer : mainBuilding.getLocEmergencyList()) {
							locEmerDto = new LocEmergencyDto();
							locEmerDto = modelMapper.map(locEmer, LocEmergencyDto.class);
							locEmerDtoList.add(locEmerDto);
						}
						mainBuildingDto.setLocEmergencyList(locEmerDtoList);
						locEmerDtoList = new ArrayList<>();
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

		return mainBuildingDtoList;
	}
	
	@Override
	public List<MainBuildingDto> getByMainBuildingIds(String id) {
		// TODO Auto-generated method stub
		
		//MainBuildingDto mainBuildingDto = new MainBuildingDto();
		
		List<MainBuildingDto> mainBuildingDtoList = new ArrayList<>();
		List<MainBuilding> mainBuildingList = new ArrayList<>();
		logger.info("Retrieving main building entity: ");
		
		List<Long> mainBuildingIds = Arrays.stream(id.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
		try {			
			mainBuildingList = mainBuildingDao.findAllLocEmergency(mainBuildingIds);			
			if(mainBuildingList != null) {
				for(MainBuilding mainBuilding : mainBuildingList) {
					MainBuildingDto mainBuildingDto = new MainBuildingDto();
					mainBuildingDto = modelMapper.map(mainBuilding, MainBuildingDto.class);
					mainBuildingDtoList.add(mainBuildingDto);
				}
			}
			

		} catch (DataAccessException dae) {
			logger.info("Error retrieving main building entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error main building entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}
		return mainBuildingDtoList;
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
