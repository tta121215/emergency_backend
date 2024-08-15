package com.emergency.rollcall.service.Impl;

import java.math.BigDecimal;
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
import com.emergency.rollcall.dao.LocEmergencyDao;
import com.emergency.rollcall.dao.RouteDao;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.RouteDto;
import com.emergency.rollcall.dto.LocEmergencyDto;
import com.emergency.rollcall.dto.LocationDto;
import com.emergency.rollcall.entity.LocEmergency;
import com.emergency.rollcall.entity.Route;
import com.emergency.rollcall.service.LocEmergencyService;

@Service
public class LocEmergencyServiceImpl implements LocEmergencyService {

	private final Logger logger = Logger.getLogger(LocEmergencyService.class.getName());

	@Autowired
	private LocEmergencyDao locEmergencyDao;

	@Autowired
	private RouteDao routeDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveLocEmergency(LocEmergencyDto locEmergencyDto) {
		ResponseDto res = new ResponseDto();
		LocEmergency locEmergency = new LocEmergency();
		List<Route> routeList = new ArrayList<>();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		locEmergency = modelMapper.map(locEmergencyDto, LocEmergency.class);

		locEmergency.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
		logger.info("Saving location emergency entity: " + locEmergencyDto);
		try {
			if (locEmergencyDto.getRouteList() != null) {
				for (RouteDto routeData : locEmergencyDto.getRouteList()) {
					Optional<Route> routeOptional = routeDao.findById(routeData.getSyskey());
					if (routeOptional.isPresent() && routeOptional.get().getSyskey() != 0) {
						routeList.add(routeOptional.get());
					} else {
						res.setStatus_code(401);
						res.setMessage("Route data is invalid.");
						return res;
					}
				}
				locEmergency.setRouteList(routeList);
			}

			if (locEmergency.getSyskey() == 0) {
				LocEmergency entityres = locEmergencyDao.save(locEmergency);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
					logger.info("Successfully saving location emergency entity: " + entityres);
				}
			} else {
				LocEmergency entityres = locEmergencyDao.save(locEmergency);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
				}
			}
		} catch (DataAccessException e) {
			logger.info("Error saving location emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error saving location emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public LocEmergencyDto getById(long id) {
		LocEmergencyDto locEmergencyDto = new LocEmergencyDto();
		LocEmergency locEmergency = new LocEmergency();
		logger.info("Retrieving location emergency entity: " + id);
		try {
			Optional<LocEmergency> LocEmergencyOptional = locEmergencyDao.findById(id);
			if (LocEmergencyOptional.isPresent()) {
				locEmergency = LocEmergencyOptional.get();
				locEmergencyDto = modelMapper.map(locEmergency, LocEmergencyDto.class);
				logger.info("Successfully retrived location emergency entity: " + locEmergencyDto);
			}

		} catch (DataAccessException dae) {
			logger.info("Error retrieving location emergency entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error location emergency entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return locEmergencyDto;
	}

	@Override
	public ResponseDto updateLocEmergency(LocEmergencyDto locEmergencyDto) {
		ResponseDto res = new ResponseDto();
		LocEmergency locEmergency = new LocEmergency();
		List<Route> routeList = new ArrayList<>();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		logger.info("Updaing location emergency entity: " + locEmergencyDto);
		try {
			Optional<LocEmergency> LocEmergencyOptional = locEmergencyDao.findById(locEmergencyDto.getSyskey());
			if (LocEmergencyOptional.isPresent()) {
				locEmergency = LocEmergencyOptional.get();
				createdDate = locEmergency.getCreateddate();
				locEmergency = modelMapper.map(locEmergencyDto, LocEmergency.class);
				locEmergency.setCreateddate(createdDate);
				locEmergency.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				if (locEmergencyDto != null) {
					for (RouteDto routeData : locEmergencyDto.getRouteList()) {
						Optional<Route> routeOptional = routeDao.findById(routeData.getSyskey());
						if (routeOptional.isPresent() && routeOptional.get().getSyskey() != 0) {
							routeList.add(routeOptional.get());
						} else {
							res.setStatus_code(401);
							res.setMessage("Mode Noti data is invalid.");
							return res;
						}
					}
					locEmergency.setRouteList(routeList);
				}

				locEmergencyDao.save(locEmergency);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Successfully updating location emergency entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Data does not found location emergency entity: " + res.getMessage());
			}

		} catch (DataAccessException e) {
			logger.info("Error updating location emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error updating location emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ResponseDto deleteLocEmergency(long id) {
		ResponseDto res = new ResponseDto();
		LocEmergency locEmergency = new LocEmergency();
		logger.info("Deleting location emergency entity: " + id);
		try {
			Long count = locEmergencyDao.countEmergencyActivatesByEmergencyLocationId(id);

			if (count > 0) {
				res.setStatus_code(200);
				res.setMessage("Cannot delete the emergency location because it is associated with active emergencies.");
				return res;
			}
			
			Optional<LocEmergency> LocEmergencyOptional = locEmergencyDao.findById(id);
			if (LocEmergencyOptional.isPresent()) {
				locEmergency = LocEmergencyOptional.get();
				locEmergency.setRouteList(new ArrayList<>());
				locEmergencyDao.save(locEmergency);
				locEmergencyDao.delete(locEmergency);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Successfully deleted location emergency entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("No data found location emergency entity: " + res.getMessage());
			}
		} catch (DataAccessException e) {
			logger.info("Error deleting location emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error deleting location emergency entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public Page<LocEmergencyDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<LocEmergency> locEmergencyList;
		List<LocEmergencyDto> locEmergencyDtoList = new ArrayList<>();
		List<RouteDto> routeDtoList = new ArrayList<>();
		RouteDto routeDto = new RouteDto();
		logger.info("Searching location emergency entity: ");
		try {
			if (params == null || params.isEmpty()) {
				locEmergencyList = locEmergencyDao.findByNameOrCode(pageRequest);
			} else {
				locEmergencyList = locEmergencyDao.findByNameOrCode(pageRequest, params);
			}
			if (locEmergencyList != null) {
				for (LocEmergency locEmergency : locEmergencyList) {
					LocEmergencyDto locEmergencyDto = new LocEmergencyDto();
					locEmergencyDto = modelMapper.map(locEmergency, LocEmergencyDto.class);
					if (locEmergency.getRouteList() != null) {
						for (Route route : locEmergency.getRouteList()) {
							routeDto = new RouteDto();
							routeDto = modelMapper.map(route, RouteDto.class);
							routeDtoList.add(routeDto);
						}
						locEmergencyDto.setRouteList(routeDtoList);
						routeDtoList = new ArrayList<>();
					}
					locEmergencyDtoList.add(locEmergencyDto);
					logger.info("Successfully searching location emergency entity: " + locEmergencyDtoList);
				}
			}
		} catch (DataAccessException dae) {
			logger.info("Error retrieving location emergency entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error retrieving location emergency entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}
		return new PageImpl<>(locEmergencyDtoList, pageRequest, locEmergencyList.getTotalElements());
	}

	@Override
	public List<LocEmergencyDto> getAllList() {

		List<LocEmergencyDto> locEmergencyDtoList = new ArrayList<>();
		List<LocEmergency> locEmergencyList = new ArrayList<>();
		try {
			locEmergencyList = locEmergencyDao.findAllByStatusAndIsDelete(1, 0);
			if (locEmergencyList != null) {
				for (LocEmergency locEmergency : locEmergencyList) {
					LocEmergencyDto locEmergencyDto = new LocEmergencyDto();
					locEmergencyDto = modelMapper.map(locEmergency, LocEmergencyDto.class);
					locEmergencyDtoList.add(locEmergencyDto);
				}
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return locEmergencyDtoList;
	}

	@Override
	public List<LocationDto> getAllLocationList() {
		List<LocationDto> locationList = new ArrayList<>();
		try {
			List<Object[]> results = locEmergencyDao.findAllLocationList();
			
			locationList = results.stream()
            .map(result -> new LocationDto(
            		((BigDecimal) result[0]).longValue(), 
            		(String) result[1])
            	)
            .collect(Collectors.toList());
			
		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}
		return locationList;
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
