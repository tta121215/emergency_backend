package com.emergency.rollcall.service.Impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.emergency.rollcall.dao.AssemblyDao;
import com.emergency.rollcall.dao.ConditionDao;
import com.emergency.rollcall.dao.EmergencyActivateDao;
import com.emergency.rollcall.dao.EmergencyDao;
import com.emergency.rollcall.dao.LocEmergencyDao;
import com.emergency.rollcall.dao.RouteDao;
import com.emergency.rollcall.dto.AssemblyDto;
import com.emergency.rollcall.dto.ConditionDto;
import com.emergency.rollcall.dto.EmergencyActivateDto;
import com.emergency.rollcall.dto.EmergencyDto;
import com.emergency.rollcall.dto.LocEmergencyDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.RouteDto;
import com.emergency.rollcall.entity.Assembly;
import com.emergency.rollcall.entity.Condition;
import com.emergency.rollcall.entity.Emergency;
import com.emergency.rollcall.entity.EmergencyActivate;
import com.emergency.rollcall.entity.LocEmergency;
import com.emergency.rollcall.entity.Route;
import com.emergency.rollcall.service.EmergencyActivateService;

@Service
public class EmergencyActviateServiceImpl implements EmergencyActivateService {

	@Autowired
	private EmergencyActivateDao emergencyActivateDao;

	@Autowired
	private AssemblyDao assemblyDao;

	@Autowired
	private RouteDao routeDao;

	@Autowired
	private EmergencyDao emergencyDao;

	@Autowired
	private ConditionDao conditionDao;

	@Autowired
	private LocEmergencyDao locEmergencyDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveEmergencyActivate(EmergencyActivateDto eActivateDto) {
		ResponseDto res = new ResponseDto();
		EmergencyActivate eActivate = new EmergencyActivate();
		Emergency emergency = new Emergency();
		Condition condition = new Condition();
		LocEmergency locEmergency = new LocEmergency();
		List<Assembly> assemblyList = new ArrayList<>();
		List<Route> routeList = new ArrayList<>();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		try {
			eActivate = modelMapper.map(eActivateDto, EmergencyActivate.class);

			eActivate.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
			if (eActivateDto.getAssemblyDtoList() != null) {
				for (AssemblyDto assemblyDto : eActivateDto.getAssemblyDtoList()) {
					Optional<Assembly> assemblyOptional = assemblyDao.findById(assemblyDto.getSyskey());
					if (assemblyOptional.isPresent() && assemblyOptional.get().getSyskey() != 0) {
						assemblyList.add(assemblyOptional.get());
					} else {
						res.setStatus_code(401);
						res.setMessage("Assembly data is invalid.");
						return res;
					}
				}
				eActivate.setAssemblyList(assemblyList);
			}
			if (eActivateDto.getRouteDtoList() != null) {
				for (RouteDto routeDto : eActivateDto.getRouteDtoList()) {
					Optional<Route> routeOptional = routeDao.findById(routeDto.getSyskey());
					if (routeOptional.isPresent() && routeOptional.get().getSyskey() != 0) {
						routeList.add(routeOptional.get());
					} else {
						res.setStatus_code(401);
						res.setMessage("Route data is invalid.");
						return res;
					}
				}
				eActivate.setRouteList(routeList);
			}
			if (eActivateDto.getEmergency_syskey() != 0) {
				Optional<Emergency> emergencyOptional = emergencyDao.findById(eActivateDto.getEmergency_syskey());
				if (emergencyOptional.isPresent()) {
					emergency = emergencyOptional.get();
					eActivate.setEmergency(emergency);
				} else {
					res.setStatus_code(401);
					res.setMessage("Emergency data is invalid.");
					return res;
				}
			}
			if (eActivateDto.getCondition_syskey() != 0) {
				Optional<Condition> conditionOptional = conditionDao.findById(eActivateDto.getCondition_syskey());
				if (conditionOptional.isPresent()) {
					condition = conditionOptional.get();
					eActivate.setCondition(condition);
				} else {
					res.setStatus_code(401);
					res.setMessage("Condition data is invalid.");
					return res;
				}
			}
			if (eActivateDto.getLocemrgency_syskey() != 0) {
				Optional<LocEmergency> locEmgerencyOptional = locEmergencyDao
						.findById(eActivateDto.getLocemrgency_syskey());
				if (locEmgerencyOptional.isPresent()) {
					locEmergency = locEmgerencyOptional.get();
					eActivate.setLocEmergency(locEmergency);
				} else {
					res.setStatus_code(401);
					res.setMessage("Location of Emergency data is invalid.");
					return res;
				}
			}

			if (eActivate.getSyskey() == 0) {
				EmergencyActivate entityres = emergencyActivateDao.save(eActivate);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
				}
			} else {
				EmergencyActivate entityres = emergencyActivateDao.save(eActivate);
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
	public EmergencyActivateDto getById(long id) {
		EmergencyActivateDto emergencyAcivateDto = new EmergencyActivateDto();
		try {
			Optional<EmergencyActivate> eActivateOptional = emergencyActivateDao.findById(id);
			if (eActivateOptional.isPresent()) {
				EmergencyActivate eActivate = eActivateOptional.get();
				emergencyAcivateDto = modelMapper.map(eActivate, EmergencyActivateDto.class);

				if (eActivate.getEmergency() != null) {
					emergencyAcivateDto.setEmergency_syskey(eActivate.getEmergency().getSyskey());
					EmergencyDto emergencyDto = modelMapper.map(eActivate.getEmergency(), EmergencyDto.class);
					emergencyAcivateDto.setEmergencyDto(emergencyDto);
				}
				if (eActivate.getCondition() != null) {
					emergencyAcivateDto.setCondition_syskey(eActivate.getCondition().getSyskey());
					ConditionDto conditionDto = modelMapper.map(eActivate.getCondition(), ConditionDto.class);
					emergencyAcivateDto.setConditionDto(conditionDto);
				}

				if (eActivate.getLocEmergency() != null) {
					emergencyAcivateDto.setLocemrgency_syskey(eActivate.getLocEmergency().getSyskey());
					LocEmergencyDto locEmergencyDto = modelMapper.map(eActivate.getLocEmergency(),
							LocEmergencyDto.class);
					emergencyAcivateDto.setLocEmergencyDto(locEmergencyDto);
				}

				List<Assembly> assemblies = assemblyDao.findByEmergencyActivateId(eActivate.getSyskey());
				List<Route> routes = routeDao.findByEmergencyActivateId(eActivate.getSyskey());

				List<AssemblyDto> assemblyDtos = assemblies.stream()
						.map(assembly -> modelMapper.map(assembly, AssemblyDto.class)).collect(Collectors.toList());
				List<RouteDto> routeDtos = routes.stream().map(route -> modelMapper.map(route, RouteDto.class))
						.collect(Collectors.toList());

				emergencyAcivateDto.setAssemblyDtoList(assemblyDtos);
				emergencyAcivateDto.setRouteDtoList(routeDtos);
			}
			return emergencyAcivateDto;
		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

	}

	@Override
	public ResponseDto updateEmergencyActivate(EmergencyActivateDto eActivateDto) {
		ResponseDto res = new ResponseDto();
		List<Assembly> assemblyList = new ArrayList<>();
		List<Route> routeList = new ArrayList<>();
		EmergencyActivate eActivate = new EmergencyActivate();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		try {
			Optional<EmergencyActivate> eActivateOptional = emergencyActivateDao.findById(eActivateDto.getSyskey());
			if (eActivateOptional.isPresent()) {
				eActivate = eActivateOptional.get();
				createdDate = eActivate.getCreateddate();
				eActivate = modelMapper.map(eActivateDto, EmergencyActivate.class);
				eActivate.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				eActivate.setCreateddate(createdDate);
				if (eActivateDto.getAssemblyDtoList() != null) {
					for (AssemblyDto assemblyDto : eActivateDto.getAssemblyDtoList()) {
						Optional<Assembly> assemblyOptional = assemblyDao.findById(assemblyDto.getSyskey());
						if (assemblyOptional.isPresent() && assemblyOptional.get().getSyskey() != 0) {
							assemblyList.add(assemblyOptional.get());
						} else {
							res.setStatus_code(401);
							res.setMessage("Assembly data is invalid.");
							return res;
						}
					}
					eActivate.setAssemblyList(assemblyList);
				}
				if (eActivateDto.getRouteDtoList() != null) {
					for (RouteDto routeDto : eActivateDto.getRouteDtoList()) {
						Optional<Route> routeOptional = routeDao.findById(routeDto.getSyskey());
						if (routeOptional.isPresent() && routeOptional.get().getSyskey() != 0) {
							routeList.add(routeOptional.get());
						} else {
							res.setStatus_code(401);
							res.setMessage("Route data is invalid.");
							return res;
						}
					}
					eActivate.setAssemblyList(assemblyList);
				}
				if (eActivateDto.getEmergency_syskey() != 0) {
					Optional<Emergency> emergencyOptional = emergencyDao.findById(eActivateDto.getEmergency_syskey());
					if (emergencyOptional.isPresent()) {
						Emergency emergency = emergencyOptional.get();
						eActivate.setEmergency(emergency);
					} else {
						res.setStatus_code(401);
						res.setMessage("Emergency data is invalid.");
						return res;
					}
				}
				if (eActivateDto.getCondition_syskey() != 0) {
					Optional<Condition> conditionOptional = conditionDao.findById(eActivateDto.getCondition_syskey());
					if (conditionOptional.isPresent()) {
						Condition condition = conditionOptional.get();
						eActivate.setCondition(condition);
					} else {
						res.setStatus_code(401);
						res.setMessage("Condition data is invalid.");
						return res;
					}
				}
				if (eActivateDto.getLocemrgency_syskey() != 0) {
					Optional<LocEmergency> locEmergencyOptional = locEmergencyDao
							.findById(eActivateDto.getLocemrgency_syskey());
					if (locEmergencyOptional.isPresent()) {
						LocEmergency locEmergency = locEmergencyOptional.get();
						eActivate.setLocEmergency(locEmergency);
					} else {
						res.setStatus_code(401);
						res.setMessage("Location of emergency data is invalid.");
						return res;
					}
				}

				emergencyActivateDao.save(eActivate);
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
	public ResponseDto deleteEmergencyActivate(long id) {
		ResponseDto res = new ResponseDto();
		EmergencyActivate eActivate = new EmergencyActivate();
		try {
			Optional<EmergencyActivate> eActivateOptional = emergencyActivateDao.findById(id);
			if (eActivateOptional.isPresent()) {
				eActivate = eActivateOptional.get();
				eActivate.setAssemblyList(new ArrayList<>());
				eActivate.setRouteList(new ArrayList<>());
				emergencyActivateDao.save(eActivate);
				emergencyActivateDao.delete(eActivate);
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
	public Page<EmergencyActivateDto> searchByParams(int page, int size, String params, String sortBy,
			String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;    
	    
	    Sort sort = Sort.by(sortDirection, sortBy);
	    if (sortBy.equals("emergency.name")) {
	        sort = Sort.by(sortDirection, "emergency.name");
	    } else if (sortBy.equals("condition.name")) {
	        sort = Sort.by(sortDirection, "condition.name");
	    }  else if (sortBy.equals("locEmergency.name")) {
	        sort = Sort.by(sortDirection, "locEmergency.name");
	    } else {
	        sort = Sort.by(sortDirection, "name");
	    }

	    PageRequest pageRequest = PageRequest.of(page, size, sort);
	    Page<EmergencyActivate> emergencyList;
	    List<EmergencyActivateDto> emergencyActivateDtoList = new ArrayList<>();

	    try {
	        if (params == null || params.isEmpty()) {
	            emergencyList = emergencyActivateDao.findAll(pageRequest); 
	        } else {
	            emergencyList = emergencyActivateDao.findByNameandRemark(pageRequest, params);
	        }

	        if (emergencyList != null) {
	            for (EmergencyActivate eActivate : emergencyList) {
	                EmergencyActivateDto eActivateDto = modelMapper.map(eActivate, EmergencyActivateDto.class);
	                
	                if (eActivate.getAssemblyList() != null) {
	                    List<AssemblyDto> assemblyDtoList = eActivate.getAssemblyList().stream()
	                            .map(assembly -> modelMapper.map(assembly, AssemblyDto.class))
	                            .collect(Collectors.toList());
	                    eActivateDto.setAssemblyDtoList(assemblyDtoList);
	                }
	               
	                if (eActivate.getRouteList() != null) {
	                    List<RouteDto> routeDtoList = eActivate.getRouteList().stream()
	                            .map(route -> modelMapper.map(route, RouteDto.class))
	                            .collect(Collectors.toList());
	                    eActivateDto.setRouteDtoList(routeDtoList);
	                }
	               
	                if (eActivate.getEmergency() != null && eActivate.getEmergency().getSyskey() != 0) {
	                    EmergencyDto emergencyDto = modelMapper.map(eActivate.getEmergency(), EmergencyDto.class);
	                    eActivateDto.setEmergencyDto(emergencyDto);
	                }
	                
	                if (eActivate.getCondition() != null && eActivate.getCondition().getSyskey() != 0) {
	                    ConditionDto conditionDto = modelMapper.map(eActivate.getCondition(), ConditionDto.class);
	                    eActivateDto.setConditionDto(conditionDto);
	                }
	                
	                if (eActivate.getLocEmergency() != null && eActivate.getLocEmergency().getSyskey() != 0) {
	                    LocEmergencyDto locEmergencyDto = modelMapper.map(eActivate.getLocEmergency(), LocEmergencyDto.class);
	                    eActivateDto.setLocEmergencyDto(locEmergencyDto);
	                }

	                emergencyActivateDtoList.add(eActivateDto);
	            }
	        }
	    } catch (DataAccessException dae) {
	        System.err.println("Database error occurred: " + dae.getMessage());
	        throw new RuntimeException("Database error occurred, please try again later.", dae);
	    } catch (Exception e) {
	        System.err.println("An unexpected error occurred: " + e.getMessage());
	        throw new RuntimeException("An unexpected error occurred, please try again later.", e);
	    }

	    return new PageImpl<>(emergencyActivateDtoList, pageRequest, emergencyList.getTotalElements());
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
