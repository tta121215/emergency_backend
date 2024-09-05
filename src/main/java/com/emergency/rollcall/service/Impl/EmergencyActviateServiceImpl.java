package com.emergency.rollcall.service.Impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.management.modelmbean.ModelMBeanInfoSupport;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.aspectj.weaver.ArrayAnnotationValue;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.emergency.rollcall.dao.AssemblyCheckInDao;
import com.emergency.rollcall.dao.AssemblyDao;
import com.emergency.rollcall.dao.ConditionDao;
import com.emergency.rollcall.dao.ContentNotiDao;
import com.emergency.rollcall.dao.EmergencyActivateDao;
import com.emergency.rollcall.dao.EmergencyDao;
import com.emergency.rollcall.dao.LocEmergencyDao;
import com.emergency.rollcall.dao.MainBuildingDao;
import com.emergency.rollcall.dao.ModeNotiDao;
import com.emergency.rollcall.dao.NotiTemplateDao;
import com.emergency.rollcall.dao.RouteDao;
import com.emergency.rollcall.dao.SubjectNotiDao;
import com.emergency.rollcall.dto.AssemblyDto;
import com.emergency.rollcall.dto.ConditionDto;
import com.emergency.rollcall.dto.EActivateSubjectDto;
import com.emergency.rollcall.dto.EActivationDto;
import com.emergency.rollcall.dto.EmailResponse;
import com.emergency.rollcall.dto.EmergencyActivateDto;
import com.emergency.rollcall.dto.EmergencyDto;
import com.emergency.rollcall.dto.EmergencyRollCallDto;
import com.emergency.rollcall.dto.LocEmergencyDto;
import com.emergency.rollcall.dto.MainBuildingDto;
import com.emergency.rollcall.dto.MessageRequestDto;
import com.emergency.rollcall.dto.ModeNotiDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.RouteDto;
import com.emergency.rollcall.entity.Assembly;
import com.emergency.rollcall.entity.Condition;
import com.emergency.rollcall.entity.ContentNoti;
import com.emergency.rollcall.entity.Emergency;
import com.emergency.rollcall.entity.EmergencyActivate;
import com.emergency.rollcall.entity.LocEmergency;
import com.emergency.rollcall.entity.MainBuilding;
import com.emergency.rollcall.entity.ModeNoti;
import com.emergency.rollcall.entity.NotiTemplate;
import com.emergency.rollcall.entity.Route;
import com.emergency.rollcall.entity.SubjectNoti;
import com.emergency.rollcall.service.EmergencyActivateService;
import com.google.gson.Gson;

import reactor.core.publisher.Mono;

@Service
public class EmergencyActviateServiceImpl implements EmergencyActivateService {

	private final Logger logger = Logger.getLogger(EmergencyActivateService.class.getName());
	

	private static WebClient webClient = null;

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
	private NotiTemplateDao notitemplateDao;

	@Autowired
	private ModeNotiDao modeNotiDao;

	@Autowired
	private SubjectNotiDao subjectNotiDao;

	@Autowired
	private ContentNotiDao contentNotiDao;

	@Autowired
	private AssemblyCheckInDao assemblyCheckInDao;
	
	@Autowired
	private MainBuildingDao mainBuildingDao;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ApplicationContext applicationContext;
	
	

	@Value("${emergency.message.url}")
	private String emergencyMessageUrl;

	@Autowired
	public EmergencyActviateServiceImpl(WebClient.Builder webClientBuilder) {
		webClient = webClientBuilder.baseUrl("http://cenvirotrial.cenviro.com:8080").build();
	}

	@Override
	public ResponseDto saveEmergencyActivate(EmergencyActivateDto eActivateDto) {
		ResponseDto res = new ResponseDto();
		EmergencyActivate eActivate = new EmergencyActivate();
		Emergency emergency = new Emergency();
		Condition condition = new Condition();
		List<Assembly> assemblyList = new ArrayList<>();
		List<Route> routeList = new ArrayList<>();
		List<LocEmergency> locEmergencyList = new ArrayList<>();
		List<MainBuilding> mainBuildingList = new ArrayList<>();
		logger.info("Saving Emergency entity: " + eActivateDto);
		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String strCreatedDate = malaysiaDateTime.format(formatter);
		String strDatetime = malaysiaDateTime.format(timeformatter);		
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
			if (eActivateDto.getLocEmergencyDtoList() != null) {
				for (LocEmergencyDto locEmergencyDto : eActivateDto.getLocEmergencyDtoList()) {
					Optional<LocEmergency> locEmergencyOptional = locEmergencyDao.findById(locEmergencyDto.getSyskey());
					if (locEmergencyOptional.isPresent() && locEmergencyOptional.get().getSyskey() != 0) {
						locEmergencyList.add(locEmergencyOptional.get());
					} else {
						res.setStatus_code(401);
						res.setMessage("Location of emergency data is invalid.");
						return res;
					}
				}
				eActivate.setLocEmergencyList(locEmergencyList);
			}
			
			if (eActivateDto.getMainBuildingDtoList() != null) {
				for (MainBuildingDto mainBuildingDto : eActivateDto.getMainBuildingDtoList()) {
					Optional<MainBuilding> mainOptional = mainBuildingDao.findById(eActivateDto.getSyskey());
					if (mainOptional.isPresent() && mainOptional.get().getSyskey() != 0) {
						mainBuildingList.add(mainOptional.get());
					} else {
						res.setStatus_code(401);
						res.setMessage("Main Building of emergency data is invalid.");
						return res;
					}
				}
				eActivate.setMainBuildingList(mainBuildingList);
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

			if (eActivate.getSyskey() == 0) {
				EmergencyActivate entityres = emergencyActivateDao.save(eActivate);
				if (entityres.getSyskey() > 0) {
					System.out.println("syskey " + entityres.getSyskey());
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
					res.setEmergencySyskey(entityres.getSyskey());
					logger.info("Saving emergency activate entity: " + entityres);
				}
			} else {
				EmergencyActivate entityres = emergencyActivateDao.save(eActivate);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
					logger.info("Saving emergency activate entity: " + entityres);
				}
			}

		} catch (DataAccessException e) {
			logger.info("Error saving emergency activate entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error saving emergency activate entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public EmergencyActivateDto getById(long id) {
		EmergencyActivateDto emergencyAcivateDto = new EmergencyActivateDto();
		logger.info("Searching emergency activate entity: " + id);
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

				List<Assembly> assemblies = assemblyDao.findByEmergencyActivateId(eActivate.getSyskey());
				List<Route> routes = routeDao.findByEmergencyActivateId(eActivate.getSyskey());
				List<LocEmergency> locEmergency = locEmergencyDao.findByEmergencyActivateId(eActivate.getSyskey());
				List<MainBuilding> mainBuilding = mainBuildingDao.findByEmergencyActivateId(eActivate.getSyskey())

				List<AssemblyDto> assemblyDtos = assemblies.stream()
						.map(assembly -> modelMapper.map(assembly, AssemblyDto.class)).collect(Collectors.toList());
				List<RouteDto> routeDtos = routes.stream().map(route -> modelMapper.map(route, RouteDto.class))
						.collect(Collectors.toList());

				List<LocEmergencyDto> locEmergencyDtos = locEmergency.stream()
						.map(locemergency -> modelMapper.map(locemergency, LocEmergencyDto.class))
						.collect(Collectors.toList());
				
				List<MainBuildingDto> mainBuildingDtos = mainBuilding.stream()
						.map(mainBuilding -> modelMapper.map(mainBuilding, MainBuildingDto.class))
						.collect(Collectors.toList());
				emergencyAcivateDto.setAssemblyDtoList(assemblyDtos);
				emergencyAcivateDto.setRouteDtoList(routeDtos);
				emergencyAcivateDto.setLocEmergencyDtoList(locEmergencyDtos);
				emergencyAcivateDto.setMainBuildingDtoList(mainBuildingDtos);
				List<Long> locemergency = new ArrayList<>();
				for (LocEmergencyDto loce : locEmergencyDtos) {
					locemergency.add(loce.getSyskey());
				}
				emergencyAcivateDto.setLocemrgency_syskey(locemergency);
			}
			logger.info("Retrieving emergency activate entity: " + emergencyAcivateDto);
			return emergencyAcivateDto;
		} catch (DataAccessException dae) {
			logger.info("Error retrieving emergency activate entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error saving emergency activate entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

	}

	@Override
	public ResponseDto updateEmergencyActivate(EmergencyActivateDto eActivateDto) {
		ResponseDto res = new ResponseDto();
		List<Assembly> assemblyList = new ArrayList<>();
		List<Route> routeList = new ArrayList<>();
		List<LocEmergency> locEmergencyList = new ArrayList<>();
		List<MainBuilding> mainBuildingList = new ArrayList<>();
		EmergencyActivate eActivate = new EmergencyActivate();
		String createdDate;
		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String strCreatedDate = malaysiaDateTime.format(formatter);
		String strDatetime = malaysiaDateTime.format(timeformatter);	
		logger.info("Updating emergency activate entity: " + eActivateDto);
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
					eActivate.setRouteList(routeList);
				}
				if (eActivateDto.getLocEmergencyDtoList() != null) {
					for (LocEmergencyDto locEmergencyDto : eActivateDto.getLocEmergencyDtoList()) {
						Optional<LocEmergency> locEmergencyOptional = locEmergencyDao
								.findById(locEmergencyDto.getSyskey());
						if (locEmergencyOptional.isPresent() && locEmergencyOptional.get().getSyskey() != 0) {
							locEmergencyList.add(locEmergencyOptional.get());
						} else {
							res.setStatus_code(401);
							res.setMessage("Route data is invalid.");
							return res;
						}
					}
					eActivate.setLocEmergencyList(locEmergencyList);
				}
				if (eActivateDto.getMainBuildingDtoList() != null) {
					for (MainBuildingDto mainDto : eActivateDto.getMainBuildingDtoList()) {
						Optional<MainBuilding> mainOptional = mainBuildingDao
								.findById(mainDto.getSyskey());
						if (mainOptional.isPresent() && mainOptional.get().getSyskey() != 0) {
							mainBuildingList.add(mainOptional.get());
						} else {
							res.setStatus_code(401);
							res.setMessage("Main Building data is invalid.");
							return res;
						}
					}
					eActivate.setMainBuildingList(mainBuildingList);
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

				emergencyActivateDao.save(eActivate);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Successfully updating emergency activate entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Data not found updating emergency activate entity: " + res.getMessage());
			}

		} catch (DataAccessException e) {
			logger.info("Error updating emergency activate entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error updating emergency activate entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ResponseDto deleteEmergencyActivate(long id) {
		ResponseDto res = new ResponseDto();
		EmergencyActivate eActivate = new EmergencyActivate();
		logger.info("Deleting emergency activate entity: " + id);
		try {
			Optional<EmergencyActivate> eActivateOptional = emergencyActivateDao.findById(id);
			if (eActivateOptional.isPresent()) {
				eActivate = eActivateOptional.get();
				eActivate.setAssemblyList(new ArrayList<>());
				eActivate.setRouteList(new ArrayList<>());
				eActivate.setLocEmergencyList(new ArrayList<>());
				emergencyActivateDao.save(eActivate);
				emergencyActivateDao.delete(eActivate);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Successfully deleting emergency activate entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("Data does not found emergency activate entity: " + res.getMessage());
			}

		} catch (DataAccessException e) {
			logger.info("Error deleting emergency activate entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error deleting emergency activate entity: " + e.getMessage());
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
		} else {
			sort = Sort.by(sortDirection, "name");
		}
		logger.info("Searching emergency activate entity: ");
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		Page<EmergencyActivate> emergencyList;
		List<EmergencyActivateDto> emergencyActivateDtoList = new ArrayList<>();

		try {
			if (params == null || params.isEmpty()) {
				emergencyList = emergencyActivateDao.findByNameandRemark(pageRequest);
			} else {
				emergencyList = emergencyActivateDao.findByNameandRemark(pageRequest, params);
			}

			if (!emergencyList.isEmpty()) {
				for (EmergencyActivate eActivate : emergencyList) {
					EmergencyActivateDto eActivateDto = modelMapper.map(eActivate, EmergencyActivateDto.class);

					if (!eActivate.getAssemblyList().isEmpty()) {
						List<AssemblyDto> assemblyDtoList = eActivate.getAssemblyList().stream()
								.map(assembly -> modelMapper.map(assembly, AssemblyDto.class))
								.collect(Collectors.toList());
						eActivateDto.setAssemblyDtoList(assemblyDtoList);
					}

					if (!eActivate.getRouteList().isEmpty()) {
						List<RouteDto> routeDtoList = eActivate.getRouteList().stream()
								.map(route -> modelMapper.map(route, RouteDto.class)).collect(Collectors.toList());
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
					if (!eActivate.getLocEmergencyList().isEmpty()) {
						String locem = "";
						for (LocEmergency loce : eActivate.getLocEmergencyList()) {
							locem += loce.getLocationName() + ",";
						}
						eActivateDto.setEmergency_location(locem.substring(0, locem.length() - 1));
					}
					if (!eActivate.getMainBuildingList().isEmpty()) {
						List<MainBuildingDto> mainDtoList = eActivate.getMainBuildingList().stream()
								.map(mainBuilding -> modelMapper.map(mainBuilding, MainBuildingDto.class)).collect(Collectors.toList());
						eActivateDto.setMainBuildingDtoList(mainDtoList);
					}
					
					emergencyActivateDtoList.add(eActivateDto);
					logger.info("Successfully searching emergency activate entity: " + emergencyActivateDtoList);
				}
			}
		} catch (DataAccessException dae) {
			logger.info("Error searching emergency activate entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching emergency activate entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(emergencyActivateDtoList, pageRequest, emergencyList.getTotalElements());
	}

	@Override
	public List<EmergencyActivateDto> getAllDashboardList() {
		logger.info("Searching emergency activate entity: ");
		List<EmergencyActivate> emergencyActivateList = new ArrayList<>();
		List<EmergencyActivateDto> emergencyActivateDtoList = new ArrayList<>();

		try {
			emergencyActivateList = emergencyActivateDao.findAllByActivateStatusAndIsDelete(2, 0);

			if (!emergencyActivateList.isEmpty()) {
				for (EmergencyActivate eActivate : emergencyActivateList) {
					EmergencyActivateDto eActivateDto = modelMapper.map(eActivate, EmergencyActivateDto.class);

					if (!eActivate.getAssemblyList().isEmpty()) {
						List<AssemblyDto> assemblyDtoList = eActivate.getAssemblyList().stream()
								.map(assembly -> modelMapper.map(assembly, AssemblyDto.class))
								.collect(Collectors.toList());
						eActivateDto.setAssemblyDtoList(assemblyDtoList);
					}

					if (!eActivate.getRouteList().isEmpty()) {
						List<RouteDto> routeDtoList = eActivate.getRouteList().stream()
								.map(route -> modelMapper.map(route, RouteDto.class)).collect(Collectors.toList());
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
					if (!eActivate.getLocEmergencyList().isEmpty()) {
						String locem = "";
						for (LocEmergency loce : eActivate.getLocEmergencyList()) {
							locem += loce.getLocationName() + ",";
						}
						eActivateDto.setEmergency_location(locem.substring(0, locem.length() - 1));
					}
					if (!eActivate.getMainBuildingList().isEmpty()) {
						List<MainBuildingDto> mainDtoList = eActivate.getMainBuildingList().stream()
								.map(mainBuilding -> modelMapper.map(mainBuilding, MainBuildingDto.class)).collect(Collectors.toList());
						eActivateDto.setMainBuildingDtoList(mainDtoList);
					}

					emergencyActivateDtoList.add(eActivateDto);
					logger.info("Successfully searching emergency activate entity: " + emergencyActivateDtoList);
				}
			}
		} catch (DataAccessException dae) {
			logger.info("Error searching emergency activate entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching emergency activate entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return emergencyActivateDtoList;
	}

	@Override
	public List<EmergencyActivateDto> getAllList(String fromDate, String toDate) {
		logger.info("Searching emergency activate entity: ");
		List<EmergencyActivate> emergencyActivateList = new ArrayList<>();
		List<EmergencyActivateDto> emergencyActivateDtoList = new ArrayList<>();
		
		try {
			if (fromDate != null && toDate != null) {
				emergencyActivateList = emergencyActivateDao.findAllByDateRange(fromDate, toDate);
			} else {
				emergencyActivateList = emergencyActivateDao.findAll(Sort.by(Sort.Direction.DESC, "syskey"));
			}

			if (!emergencyActivateList.isEmpty()) {
				for (EmergencyActivate eActivate : emergencyActivateList) {
					EmergencyActivateDto eActivateDto = modelMapper.map(eActivate, EmergencyActivateDto.class);

					if (!eActivate.getAssemblyList().isEmpty()) {
						List<AssemblyDto> assemblyDtoList = eActivate.getAssemblyList().stream()
								.map(assembly -> modelMapper.map(assembly, AssemblyDto.class))
								.collect(Collectors.toList());
						eActivateDto.setAssemblyDtoList(assemblyDtoList);
					}

					if (!eActivate.getRouteList().isEmpty()) {
						List<RouteDto> routeDtoList = eActivate.getRouteList().stream()
								.map(route -> modelMapper.map(route, RouteDto.class)).collect(Collectors.toList());
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
					if (!eActivate.getLocEmergencyList().isEmpty()) {
						String locem = "";
						for (LocEmergency loce : eActivate.getLocEmergencyList()) {
							locem += loce.getLocationName() + ",";
						}
						eActivateDto.setEmergency_location(locem.substring(0, locem.length() - 1));
					}
					if (!eActivate.getMainBuildingList().isEmpty()) {
						List<MainBuildingDto> mainDtoList = eActivate.getMainBuildingList().stream()
								.map(mainBuilding -> modelMapper.map(mainBuilding, MainBuildingDto.class)).collect(Collectors.toList());
						eActivateDto.setMainBuildingDtoList(mainDtoList);
					}
					emergencyActivateDtoList.add(eActivateDto);
					logger.info("Successfully searching emergency activate entity: " + emergencyActivateDtoList);
				}
			}
		} catch (DataAccessException dae) {
			logger.info("Error searching emergency activate entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching emergency activate entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return emergencyActivateDtoList;
	}

	@Override
	public EActivationDto emergencyActivate(long id) {
		EmergencyActivateDto emergencyAcivateDto = new EmergencyActivateDto();
		EActivationDto eActivationDto = new EActivationDto();
		logger.info("Searching emergency activate entity: " + id);		
		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String strCreatedDate = malaysiaDateTime.format(formatter);
		String strCurrentTime = malaysiaDateTime.format(timeformatter);
		try {
			Optional<EmergencyActivate> eActivateOptional = emergencyActivateDao.findById(id);
			if (eActivateOptional.isPresent()) {
				EmergencyActivate eActivate = eActivateOptional.get();
				eActivate.setStartDate(strCreatedDate);
				eActivate.setStartTime(strCurrentTime);
				eActivate.setActivateStatus(1);
				emergencyActivateDao.save(eActivate);
				emergencyAcivateDto = modelMapper.map(eActivate, EmergencyActivateDto.class);

				List<NotiTemplate> notiList = notitemplateDao.findAllByStatus(0);
				if (!notiList.isEmpty()) {
					String modeNotiMessage = null;
					NotiTemplate notiTemplate = notiList.get(0);
					if (notiTemplate.getNoti_mode() != null) {
						List<Long> modeIds = Arrays.stream(notiTemplate.getNoti_mode().split(",")).map(String::trim)
								.map(Long::parseLong).collect(Collectors.toList());
						List<ModeNoti> modeNotiList = modeNotiDao.findAllById(modeIds);						
						if (!modeNotiList.isEmpty()) {
							modeNotiMessage = modeNotiList.stream()
			                           .map(ModeNoti::getName)
			                           .collect(Collectors.joining(","));
						}						
					}
					JSONObject response = sendEmergencyMessage(modeNotiMessage, eActivate.getSyskey());
				}

			}
			logger.info("Retrieving emergency activate entity: " + emergencyAcivateDto);
			// String email = getEmail();
			// System.out.println("Email " + email);
			return eActivationDto;
		} catch (DataAccessException dae) {
			logger.info("Error retrieving emergency activate entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error saving emergency activate entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

	}

	@Override
	public ResponseDto emergencyActivateManualEnd(long id) {
		EmergencyActivateDto emergencyAcivateDto = new EmergencyActivateDto();
		ResponseDto responseDto = new ResponseDto();

		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String strCreatedDate = malaysiaDateTime.format(formatter);
		String strCurrentTime = malaysiaDateTime.format(timeformatter);

		logger.info("Searching emergency activate entity: " + id);
		try {
			Optional<EmergencyActivate> eActivateOptional = emergencyActivateDao.findById(id);
			if (eActivateOptional.isPresent()) {
				EmergencyActivate eActivate = eActivateOptional.get();
				eActivate.setEndDate(strCreatedDate);
				eActivate.setEndTime(strCurrentTime);
				eActivate.setActivateStatus(2);
				emergencyActivateDao.save(eActivate);
				responseDto.setStatus_code(200);
				responseDto.setMessage("Successfully Emergency Activate End");
				logger.info("Successfully End emergency activate entity: " + responseDto.getMessage());
			}
			logger.info("Retrieving emergency activate entity: " + emergencyAcivateDto);
			return responseDto;
		} catch (DataAccessException dae) {
			logger.info("Error retrieving emergency activate entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error saving emergency activate entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

	}

	@Override
	public EActivationDto emergencyActivateForNoti(long id) {
		EmergencyActivateDto emergencyAcivateDto = new EmergencyActivateDto();
		EActivationDto eActivationDto = new EActivationDto();
		logger.info("Searching emergency activate entity: " + id);
		try {
			Optional<EmergencyActivate> eActivateOptional = emergencyActivateDao.findById(id);
			if (eActivateOptional.isPresent()) {
				EmergencyActivate eActivate = eActivateOptional.get();
				emergencyAcivateDto = modelMapper.map(eActivate, EmergencyActivateDto.class);

				List<NotiTemplate> notiList = notitemplateDao.findAllByStatus(0);
				if (!notiList.isEmpty()) {
					NotiTemplate notiTemplate = notiList.get(0);
					if (notiTemplate.getNoti_mode() != null) {
						List<Long> modeIds = Arrays.stream(notiTemplate.getNoti_mode().split(",")).map(String::trim)
								.map(Long::parseLong).collect(Collectors.toList());
						List<ModeNoti> modeNotiList = modeNotiDao.findAllById(modeIds);
						List<ModeNotiDto> modeNotiDtoList = new ArrayList<>();
						if (!modeNotiList.isEmpty()) {
							for (ModeNoti modeNoti : modeNotiList) {
								ModeNotiDto modenotiDto = modelMapper.map(modeNoti, ModeNotiDto.class);
								modeNotiDtoList.add(modenotiDto);
							}
							eActivationDto.setModeNotiDtoList(modeNotiDtoList);
						}
					}
					// Subject List
					if (notiTemplate.getNoti_subject() != null) {
						List<Long> subjectIds = Arrays.stream(notiTemplate.getNoti_subject().split(","))
								.map(String::trim).map(Long::parseLong).collect(Collectors.toList());
						List<SubjectNoti> subjectNotiList = subjectNotiDao.findAllById(subjectIds);
						EActivateSubjectDto eactivateSubjectDto = new EActivateSubjectDto();
						if (!subjectNotiList.isEmpty()) {
							for (SubjectNoti subjectNoti : subjectNotiList) {
								if (subjectNoti.getTableName().equals("Date")) {
									eactivateSubjectDto.setDate(eActivate.getStartDate());
								}
								if (subjectNoti.getTableName().equals("Time")) {
									eactivateSubjectDto.setTime(eActivate.getStartTime());
								}
								if (subjectNoti.getTableName().equals("Others")) {
									eactivateSubjectDto.setOthersDes(subjectNoti.getDescription());
								}
								Object entities = findEntitiesByTableName(subjectNoti.getTableName());
								if (entities != null) {
									eactivateSubjectDto = processEntity(entities, eActivate, eactivateSubjectDto);
								}
							}
							eActivationDto.setEsubjectDto(eactivateSubjectDto);
						}
					}
					// Content
					if (notiTemplate.getNoti_content() != null) {
						List<Long> contentIds = Arrays.stream(notiTemplate.getNoti_content().split(","))
								.map(String::trim).map(Long::parseLong).collect(Collectors.toList());
						List<ContentNoti> contentNotiList = contentNotiDao.findAllById(contentIds);
						EActivateSubjectDto eactivateContentDto = new EActivateSubjectDto();
						if (!contentNotiList.isEmpty()) {
							for (ContentNoti contentNoti : contentNotiList) {
								if (contentNoti.getTableName().equals("Date")) {
									eactivateContentDto.setDate(eActivate.getStartDate());
								}
								if (contentNoti.getTableName().equals("Time")) {
									eactivateContentDto.setTime(eActivate.getStartTime());
								}
								if (contentNoti.getTableName().equals("Others")) {
									eactivateContentDto.setOthersDes(contentNoti.getDescription());
								}
								Object entities = findEntitiesByTableName(contentNoti.getTableName());
								if (entities != null) {
									eactivateContentDto = processEntity(entities, eActivate, eactivateContentDto);
								}
							}
							eActivationDto.setEcontentDto(eactivateContentDto);
						}
					}

				}
			}
			logger.info("Retrieving emergency activate entity: " + emergencyAcivateDto);
			return eActivationDto;
		} catch (DataAccessException dae) {
			logger.info("Error retrieving emergency activate entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error saving emergency activate entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

	}

	@Override
	public Page<EmergencyRollCallDto> emergencyRollCall(String fromdate, String todate, Long emergencyType,
			Long emergencyStatus, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<EmergencyActivate> emergencyActivateList = emergencyActivateDao.findAllByStatus(
				fromdate != null && !fromdate.isEmpty() ? fromdate : null,
				todate != null && !todate.isEmpty() ? todate : null, emergencyType, emergencyStatus, pageRequest);

		List<EmergencyRollCallDto> emergencyRollCallDtoList = emergencyActivateList.stream().map(emergencyActivate -> {
			Long emergencySyskey = emergencyActivate.getSyskey();

			List<Map<String, Object>> allUsers = assemblyCheckInDao.findAllUsers();

			List<Map<String, Object>> checkedInUsers = assemblyCheckInDao
					.findCheckedInUsersByEmergencyActivate(emergencySyskey);

			long totalCheckInCount = (long) checkedInUsers.size();
			long totalUnCheckInCount = allUsers.size() - checkedInUsers.size();

//			LocalDateTime startTime = LocalDateTime.parse(emergencyActivate.getStartTime(), formatter);
//			LocalDateTime endTime = LocalDateTime.parse(emergencyActivate.getEndTime(), formatter);
			
			ZonedDateTime startTime = ZonedDateTime.parse(emergencyActivate.getStartTime(),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Kuala_Lumpur")));
			ZonedDateTime endTime = ZonedDateTime.parse(emergencyActivate.getEndTime(),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Kuala_Lumpur")));
			Duration duration = Duration.between(startTime, endTime);
			long totalTimeInMinutes = duration.toMinutes();

			long totalTimeInSeconds = totalTimeInMinutes * 60;

			long averageTimeInSeconds = totalCheckInCount > 0 ? totalTimeInSeconds / totalCheckInCount : 0;

			Duration averageDuration1 = Duration.ofSeconds(averageTimeInSeconds);

//			long avghours = averageDuration1.toHours();
//			long avgminutes = averageDuration1.toMinutesPart();
//			long avgseconds = averageDuration1.toSecondsPart();
			
			long avghours = averageDuration1.toHours() % 24;
			long avgminutes = averageDuration1.toMinutes() % 60;
			long avgseconds = averageDuration1.getSeconds() % 60;

			StringBuilder avgTimeString = new StringBuilder();

			if (avghours > 0) {
				avgTimeString.append(avghours).append(avghours == 1 ? " hour " : " hours ");
			}

			if (avgminutes > 0) {
				avgTimeString.append(avgminutes).append(avgminutes == 1 ? " minute " : " minutes ");
			}

			if (avgseconds > 0) {
				avgTimeString.append(avgseconds).append(avgseconds == 1 ? " second" : " seconds");
			}

			if (avgTimeString.length() == 0) {
				avgTimeString.append("0 seconds");
			}

			EmergencyRollCallDto dto = new EmergencyRollCallDto();
			dto.setSyskey(emergencySyskey);
			dto.setName(emergencyActivate.getName());
			dto.setRemark(emergencyActivate.getRemark());
			dto.setActivateDate(emergencyActivate.getStartDate());
			dto.setActivateTime(emergencyActivate.getStartTime());
			dto.setTotalCheckIn(totalCheckInCount);
			dto.setTotalNotCheckIn(totalUnCheckInCount);
			dto.setTotalTime(formatDuration(duration));
			dto.setConditionName(emergencyActivate.getCondition().getName());
			dto.setAverageTime(avgTimeString.toString().trim());
			dto.setActivateStatus(emergencyActivate.getActivateStatus());
			dto.setEmergencyName(emergencyActivate.getEmergency().getName());
			if (!emergencyActivate.getLocEmergencyList().isEmpty()) {
				String locem = "";
				int count = 0;
				for (LocEmergency loce : emergencyActivate.getLocEmergencyList()) {
					if (count > 0) {
						locem += "," + loce.getLocationName();
					} else {
						locem += loce.getLocationName();
					}
					count++;
				}
				dto.setEmegencyLocation(locem);
			}
			return dto;
		}).collect(Collectors.toList());

		return new PageImpl<>(emergencyRollCallDtoList, pageRequest, emergencyActivateList.getTotalElements());
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

	private EActivateSubjectDto processEntity(Object entity, EmergencyActivate eActivate,
			EActivateSubjectDto eActivateSubjectDto) {
		if (entity instanceof Emergency) {
			if (eActivate.getEmergency() != null) {
				EmergencyDto emergencyDto = modelMapper.map(eActivate.getEmergency(), EmergencyDto.class);
				eActivateSubjectDto.setEmergencyDto(emergencyDto);
			}

		} else if (entity instanceof Condition) {
			if (eActivate.getCondition() != null) {
				ConditionDto conditionDto = modelMapper.map(eActivate.getCondition(), ConditionDto.class);
				eActivateSubjectDto.setConditionDto(conditionDto);
			}

		} else if (entity instanceof Route) {
			List<Route> routes = routeDao.findByEmergencyActivateId(eActivate.getSyskey());
			List<RouteDto> routeDtos = routes.stream().map(route -> modelMapper.map(route, RouteDto.class))
					.collect(Collectors.toList());
			eActivateSubjectDto.setRouteDtoList(routeDtos);

		} else if (entity instanceof Assembly) {
			List<Assembly> assemblies = assemblyDao.findByEmergencyActivateId(eActivate.getSyskey());
			List<AssemblyDto> assemblyDtos = assemblies.stream()
					.map(assembly -> modelMapper.map(assembly, AssemblyDto.class)).collect(Collectors.toList());
			eActivateSubjectDto.setAssemblyDtoList(assemblyDtos);
		} else if (entity instanceof LocEmergency) {
			List<LocEmergency> locEmergency = locEmergencyDao.findByEmergencyActivateId(eActivate.getSyskey());
			List<LocEmergencyDto> locEmergencyDtos = locEmergency.stream()
					.map(locemergency -> modelMapper.map(locemergency, LocEmergencyDto.class))
					.collect(Collectors.toList());
			eActivateSubjectDto.setLocEmergencyDtoList(locEmergencyDtos);
		}
		return eActivateSubjectDto;
	}
//
//	private Object findEntitiesByTableName(String repositoryName, String criteria) {
//		try {
//			String beanName = Character.toLowerCase(repositoryName.charAt(0)) + repositoryName.substring(1);
//			JpaRepository<?, Long> repository = (JpaRepository<?, Long>) applicationContext.getBean(beanName);
//			Pageable pageable = PageRequest.of(0, 1);
//			Method findAllMethod = Arrays.stream(repository.getClass().getMethods())
//					.filter(method -> method.getName().equals("findAll") && method.getParameterCount() == 1).findFirst()
//					.orElseThrow(() -> new NoSuchMethodException(
//							"Method findAll without Pageable not found on repository: " + repositoryName));
//			//Page<?> page = (Page<?>) findAllMethod.invoke(repository, pageable);
//			//return (List<?>) findAllMethod.invoke(repository);
//			//return page.hasContent() ? page.getContent().get(0) : null;
//			
//			Object criteriaObject = null;  // Placeholder for the criteria object (could be a Specification, Example, etc.)
//	        Page<?> page = (Page<?>) findAllMethod.invoke(repository, criteriaObject, pageable);
//	        return page.hasContent() ? page.getContent().get(0) : null;
//
//		} catch (Exception e) {
//			logger.info("Error finding entities for repository: " + e.getMessage());
//			// return Collections.emptyList();
//			return null;
//		}
//	}

	private Object findEntitiesByTableName(String repositoryName) {
		try {
			String beanName = Character.toLowerCase(repositoryName.charAt(0)) + repositoryName.substring(1);
			JpaRepository<?, Long> repository = (JpaRepository<?, Long>) applicationContext.getBean(beanName);
			Pageable pageable = PageRequest.of(0, 1);

			// Find the correct findAll(Pageable) method
			Method findAllMethod = Arrays.stream(repository.getClass().getMethods())
					.filter(method -> method.getName().equals("findAll") && method.getParameterCount() == 1
							&& method.getParameterTypes()[0].equals(Pageable.class))
					.findFirst().orElseThrow(() -> new NoSuchMethodException(
							"Method findAll with Pageable not found on repository: " + repositoryName));

			// Invoke the findAll method with pageable
			Page<?> page = (Page<?>) findAllMethod.invoke(repository, pageable);

			return page.hasContent() ? page.getContent().get(0) : null;

		} catch (Exception e) {
			logger.info("Error finding entities for repository: " + e.getMessage());
			return null;
		}
	}

	private String formatDuration(Duration duration) {
		long days = duration.toDays();
		long hours = duration.toHours() % 24;
		long minutes = duration.toMinutes() % 60;
		long seconds = duration.getSeconds() % 60;

		if (days > 0) {
			return days + " days " + hours + " hours";
		} else if (hours > 0) {
			return hours + " hours " + minutes + " minutes";
		} else {
			return minutes + " minutes " + seconds + " seconds";
		}
	}

	public JSONObject sendEmergencyMessage(String modeMessage, Long activateSyskey) {
		
		MessageRequestDto request = new MessageRequestDto();
		request.setSubject(modeMessage);
		request.setContent(Arrays.asList("Dear All, there is an emergency now. Please click on the link below to see the detail"));		
		request.setLink(emergencyMessageUrl + "?eActivate=" + activateSyskey);
		request.setEmergancyId(activateSyskey);
		System.out.println("Success");
		JSONObject obj = null;
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://cenvirotrial.cenviro.com:8080/api/email/sendEmergancyAlert");
        String resultMap = new Gson().toJson(request, MessageRequestDto.class);
        StringEntity stringEntity = new StringEntity(resultMap,ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpPost);
            //System.out.println("POST Response Status:: "+ httpResponse.getStatusLine().getStatusCode());
            
            org.apache.http.HttpEntity responseBodyentity = httpResponse.getEntity();
            
            String responseBodyString = EntityUtils.toString(responseBodyentity);
            
            obj=new JSONObject(responseBodyString);
            System.out.println("Response " + obj.get("status"));
        }catch (IOException e) {
            e.printStackTrace();
        }
		return obj;

//		return webClient.post()
//	            .uri("http://cenvirotrial.cenviro.com:8080/api/email/sendEmergencyAlert")
//	            .body(Mono.just(request), MessageRequestDto.class)
//	            .retrieve()
//	            .bodyToMono(EmailResponse.class)  // Map the response to ApiResponseDto
//	            .doOnSuccess(response -> {
//	                System.out.println("Request was successful: " + response.getStatus() + ", " + response.getMessage());
//	            })
//	            .doOnError(error -> {
//	                System.err.println("Request failed: " + error.getMessage());
//	            });		

	}
//	public String getEmail() {
//        Mono<String> response = webClient.get()
//                .uri("api/email/sendEmergancyAlert")
//                .retrieve()
//                .bodyToMono(String.class);
//
//        return response.block();
//    }
	
//	public EmailResponse sendEmergencyMessageUpdate(String modeMessage, Long activateSyskey) {
//
//        MessageRequestDto request = new MessageRequestDto();
//        request.setSubject(modeMessage);
//        request.setContent(Arrays.asList("Dear All, there is an emergency now. Please click on the link below to see the detail"));
//        request.setLink(emergencyMessageUrl + "?eActivate=" + activateSyskey);
//        request.setEmergencyId(activateSyskey);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//
//        HttpEntity<MessageRequestDto> entity = new HttpEntity<>(request, headers);
//
//        ResponseEntity<EmailResponse> responseEntity = restTemplate.exchange(
//                "http://cenvirotrial.cenviro.com:8080/api/email/sendEmergencyAlert",
//                HttpMethod.POST,
//                entity,
//                EmailResponse.class
//        );
//
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            EmailResponse response = responseEntity.getBody();
//            System.out.println("Request was successful: " + response.getStatus() + ", " + response.getMessage());
//            return response;
//        } else {
//            System.err.println("Request failed with status: " + responseEntity.getStatusCode());
//            return null;
//        }
//    }

}
