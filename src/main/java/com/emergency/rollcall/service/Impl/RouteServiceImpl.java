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
import com.emergency.rollcall.dao.RouteDao;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.RouteDto;
import com.emergency.rollcall.entity.Route;
import com.emergency.rollcall.service.RouteService;

@Service
public class RouteServiceImpl implements RouteService {

	@Autowired
	private RouteDao routeDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveRoute(RouteDto routeDto) {
		ResponseDto res = new ResponseDto();
		Route route = new Route();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		route = modelMapper.map(routeDto, Route.class);

		route.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
		try {
			if (route.getSyskey() == 0) {
				Route entityres = routeDao.save(route);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
				}
			} else {
				Route entityres = routeDao.save(route);
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
	public RouteDto getById(long id) {
		RouteDto routeDto = new RouteDto();
		Route route = new Route();
		try {
			Optional<Route> routeOptional = routeDao.findById(id);
			if (routeOptional.isPresent()) {
				route = routeOptional.get();
				routeDto = modelMapper.map(route, RouteDto.class);
			}
		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return routeDto;
	}

	@Override
	public ResponseDto updateRoute(RouteDto routeDto) {
		ResponseDto res = new ResponseDto();
		Route route = new Route();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		try {
			Optional<Route> routeOptional = routeDao.findById(routeDto.getSyskey());
			if (routeOptional.isPresent()) {
				route = routeOptional.get();
				createdDate = route.getCreateddate();
				route = modelMapper.map(routeDto, Route.class);
				route.setCreateddate(createdDate);
				route.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				routeDao.save(route);
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
	public ResponseDto deleteRoute(long id) {
		ResponseDto res = new ResponseDto();
		Route route = new Route();
		try {
			Optional<Route> routeOptional = routeDao.findById(id);
			if (routeOptional.isPresent()) {
				route = routeOptional.get();
				routeDao.delete(route);
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
	public Page<RouteDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<Route> routeList;
		List<RouteDto> routeDtoList = new ArrayList<>();
		try {
			if (params == null || params.isEmpty()) {
				routeList = routeDao.findByNameOrCode(pageRequest);
				if (routeList != null) {
					for (Route route : routeList) {
						RouteDto routeDto = new RouteDto();
						routeDto = modelMapper.map(route, RouteDto.class);
						routeDtoList.add(routeDto);
					}
				}

			} else {
				routeList = routeDao.findByNameOrCode(pageRequest, params);
				if (routeList != null) {
					for (Route route : routeList) {
						RouteDto routeDto = new RouteDto();
						routeDto = modelMapper.map(route, RouteDto.class);
						routeDtoList.add(routeDto);
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
		return new PageImpl<>(routeDtoList, pageRequest, routeList.getTotalElements());
	}
	
	@Override
	public List<RouteDto> getAllList() {

		List<RouteDto> routeDtoList = new ArrayList<>();
		List<Route> routeList = new ArrayList<>();
		try {
			routeList = routeDao.findAll();
			if (routeList != null) {
				for (Route route : routeList) {
					RouteDto routeDto = new RouteDto();
					routeDto = modelMapper.map(route, RouteDto.class);
					routeDtoList.add(routeDto);
				}
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return routeDtoList;
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
