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
import com.emergency.rollcall.dao.ModeNotiDao;
import com.emergency.rollcall.dao.NotificationDao;
import com.emergency.rollcall.dao.RouteDao;
import com.emergency.rollcall.dto.EmergencyDto;
import com.emergency.rollcall.dto.ModeNotiDto;
import com.emergency.rollcall.dto.NotificationDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.RouteDto;
import com.emergency.rollcall.entity.Emergency;
import com.emergency.rollcall.entity.ModeNoti;
import com.emergency.rollcall.entity.Notification;
import com.emergency.rollcall.entity.Route;
import com.emergency.rollcall.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private ModeNotiDao modeNotiDao;

	@Autowired
	private EmergencyDao emergencyDao;

	@Autowired
	private RouteDao routeDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveNotification(NotificationDto notiDto) {
		ResponseDto res = new ResponseDto();
		Notification notification = new Notification();
		List<ModeNoti> modeNoti = new ArrayList<>();
		List<Emergency> emergency = new ArrayList<>();
		List<Route> route = new ArrayList<>();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		try {
			notification = modelMapper.map(notiDto, Notification.class);

			notification.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
			if (notiDto.getModeNotiDto() != null) {
				for (ModeNotiDto modeNotiData : notiDto.getModeNotiDto()) {
					Optional<ModeNoti> modeNotiOptional = modeNotiDao.findById(modeNotiData.getSyskey());
					if (modeNotiOptional.isPresent() && modeNotiOptional.get().getSyskey() != 0) {
						modeNoti.add(modeNotiOptional.get());
					} else {
						res.setStatus_code(401);
						res.setMessage("Mode Noti data is invalid.");
						return res;
					}
				}
				notification.setModeNotiList(modeNoti);
			}

			if (notiDto.getEmergencyDto() != null) {
				for (EmergencyDto emergencyData : notiDto.getEmergencyDto()) {
					Optional<Emergency> emergencyOptional = emergencyDao.findById(emergencyData.getSyskey());
					if (emergencyOptional.isPresent() && emergencyOptional.get().getSyskey() != 0) {
						emergency.add(emergencyOptional.get());

					} else {
						res.setStatus_code(401);
						res.setMessage("Emergency data is invalid.");
						return res;
					}
				}
				notification.setEmergencyList(emergency);
			}

			if (notiDto.getRouteDto() != null) {
				for (RouteDto routeData : notiDto.getRouteDto()) {
					Optional<Route> routeOptional = routeDao.findById(routeData.getSyskey());
					if (routeOptional.isPresent() && routeOptional.get().getSyskey() != 0) {
						route.add(routeOptional.get());

					} else {
						res.setStatus_code(401);
						res.setMessage("Route data is invalid.");
						return res;
					}
				}
				notification.setRouteList(route);
			}

			if (notification.getSyskey() == 0) {
				Notification entityres = notificationDao.save(notification);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
				}
			} else {
				Notification entityres = notificationDao.save(notification);
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

//	@Override
//	public NotificationDto getById(long id) {
//		NotificationDto notificationDto = new NotificationDto();
//		Notification notification = new Notification();
//		List<ModeNotiDto> modeNotiDTOList = new ArrayList<>();
//		Optional<Notification> notificationOptional = notificationDao.findById(id);
//		if (notificationOptional.isPresent()) {
//			notification = notificationOptional.get();
//			if (notification.getModeNotiList() != null) {
//				for (ModeNoti modeNotiData : notification.getModeNotiList()) {
//					ModeNotiDto modeNotiDTO = new ModeNotiDto();
//					modeNotiDTO.setSyskey(modeNotiData.getSyskey());
//					modeNotiDTO.setName(modeNotiData.getName());
//					modeNotiDTO.setDescription(modeNotiData.getDescription());
//					modeNotiDTO.setStatus(modeNotiData.getStatus());
//					modeNotiDTO.setCreateddate(modeNotiData.getCreateddate());
//					modeNotiDTO.setModifieddate(modeNotiData.getModifieddate());
//					modeNotiDTOList.add(modeNotiDTO);
//				}
//			}
//			notificationDto.setModeNotiDto(modeNotiDTOList);
//			notificationDto.setSyskey(notification.getSyskey());
//			notificationDto.setNotimessage(notification.getNotimessage());
//			notificationDto.setCreateddate(notification.getCreateddate());
//			notificationDto.setModifieddate(notification.getModifieddate());
//			notificationDto.setNotimode(notification.getNotimode());
//			notificationDto.setNotisubject(notification.getNotisubject());
//			notificationDto.setStatus(notification.getStatus());
//		}
//		return notificationDto;
//	}
	@Override
	public NotificationDto getById(long id) {
		NotificationDto notificationDto = new NotificationDto();
		try {
			Optional<Notification> notificationOptional = notificationDao.findById(id);
			if (notificationOptional.isPresent()) {
				Notification notification = notificationOptional.get();
				notificationDto = modelMapper.map(notification, NotificationDto.class);
			}
			return notificationDto;
		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

	}

	@Override
	public ResponseDto updateNotification(NotificationDto notiDto) {
		ResponseDto res = new ResponseDto();
		List<ModeNoti> modeNotiList = new ArrayList<>();
		List<Emergency> emergencyList = new ArrayList<>();
		List<Route> routeList = new ArrayList<>();
		Notification notification = new Notification();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		try {
			Optional<Notification> notiOptional = notificationDao.findById(notiDto.getSyskey());
			if (notiOptional.isPresent()) {
				notification = notiOptional.get();
				createdDate = notification.getCreateddate();
				notification = modelMapper.map(notiDto, Notification.class);
				notification.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				notification.setCreateddate(createdDate);
				if (notiDto.getModeNotiDto() != null) {
					for (ModeNotiDto modeNotiDto : notiDto.getModeNotiDto()) {
						Optional<ModeNoti> modeNotiOptional = modeNotiDao.findById(modeNotiDto.getSyskey());
						if (modeNotiOptional.isPresent() && modeNotiOptional.get().getSyskey() != 0) {
							modeNotiList.add(modeNotiOptional.get());
						} else {
							res.setStatus_code(401);
							res.setMessage("Mode Noti data is invalid.");
							return res;
						}
					}
					notification.setModeNotiList(modeNotiList);
				}
				if (notiDto.getEmergencyDto() != null) {
					for (EmergencyDto emergencyDto : notiDto.getEmergencyDto()) {
						Optional<Emergency> emergencyOptional = emergencyDao.findById(emergencyDto.getSyskey());
						if (emergencyOptional.isPresent() && emergencyOptional.get().getSyskey() != 0) {
							emergencyList.add(emergencyOptional.get());
						} else {
							res.setStatus_code(401);
							res.setMessage("Mode Noti data is invalid.");
							return res;
						}
					}
					notification.setEmergencyList(emergencyList);
				}
				if (notiDto.getRouteDto() != null) {
					for (RouteDto routeDto : notiDto.getRouteDto()) {
						Optional<Route> routeOptional = routeDao.findById(routeDto.getSyskey());
						if (routeOptional.isPresent() && routeOptional.get().getSyskey() != 0) {
							routeList.add(routeOptional.get());
						} else {
							res.setStatus_code(401);
							res.setMessage("Mode Noti data is invalid.");
							return res;
						}
					}
					notification.setRouteList(routeList);
				}
				notificationDao.save(notification);
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

//	 public ResponseDto updateNotification(NotificationDto notiDto) {
//	        ResponseDto res = new ResponseDto();
//	        Notification notification = new Notification();
//	        String createdDate;
//	        LocalDateTime dateTime = LocalDateTime.now();
//	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//	        String strCreatedDate = dateTime.format(formatter);
//
//	        Optional<Notification> notiOptional = notificationDao.findById(notiDto.getSyskey());
//	        if (notiOptional.isPresent()) {
//	            notification = notiOptional.get();
//	            createdDate = notification.getCreateddate();
//
//	            // Map updated values from DTO to the existing entity
//	            mapperUtil.getModelMapper().map(notiDto, notification);
//
//	            // Update the modeNotiList
//	            List<ModeNoti> modeNotis = notiDto.getModeNotiDto().stream()
//	                .map(modeNotiDto -> modeNotiDao.findById(modeNotiDto.getSyskey())
//	                .orElseThrow(() -> new RuntimeException("ModeNoti not found with syskey: " + modeNotiDto.getSyskey())))
//	                .collect(Collectors.toList());
//
//	            notification.setModeNotiList(modeNotis);
//
//	            notificationDao.save(notification);
//
//	            res.setStatus_code(200);
//	            res.setMessage("Successfully Updated");
//	        } else {
//	            res.setStatus_code(401);
//	            res.setMessage("Data does not found");
//	        }
//
//	        return res;
//	    }

	@Override
	public ResponseDto deleteNotification(long id) {
		ResponseDto res = new ResponseDto();
		Notification notification = new Notification();
		try {
			Optional<Notification> notiOptional = notificationDao.findById(id);
			if (notiOptional.isPresent()) {
				notification = notiOptional.get();
				notification.setModeNotiList(new ArrayList<>());
				notification.setEmergencyList(new ArrayList<>());
				notification.setRouteList(new ArrayList<>());
				notificationDao.save(notification);
				notificationDao.delete(notification);
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
	public Page<NotificationDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<Notification> notiList;
		List<NotificationDto> notiDtoList = new ArrayList<>();
		List<ModeNotiDto> modeNotiDtoList = new ArrayList<>();
		List<EmergencyDto> emergencyDtoList = new ArrayList<>();
		List<RouteDto> routeDtoList = new ArrayList<>();
		ModeNotiDto modeNotiDto = new ModeNotiDto();
		EmergencyDto emergencyDto = new EmergencyDto();
		RouteDto routeDto = new RouteDto();
		try {
			if (params == null || params.isEmpty()) {
				notiList = notificationDao.findByNotisubject(pageRequest);
				if (notiList != null) {
					for (Notification notification : notiList) {
						NotificationDto notiDto = new NotificationDto();
						notiDto = modelMapper.map(notification, NotificationDto.class);
						if (notification.getModeNotiList() != null) {
							for (ModeNoti modeNoti : notification.getModeNotiList()) {
								modeNotiDto = modelMapper.map(modeNoti, ModeNotiDto.class);
								modeNotiDtoList.add(modeNotiDto);
							}
							notiDto.setModeNotiDto(modeNotiDtoList);
						}
						if (notification.getEmergencyList() != null) {
							for (Emergency emergency : notification.getEmergencyList()) {
								emergencyDto = modelMapper.map(emergency, EmergencyDto.class);
								emergencyDtoList.add(emergencyDto);
							}
							notiDto.setModeNotiDto(modeNotiDtoList);
						}
						if (notification.getRouteList() != null) {
							for (Route route : notification.getRouteList()) {
								routeDto = modelMapper.map(route, RouteDto.class);
								routeDtoList.add(routeDto);
							}
							notiDto.setRouteDto(routeDtoList);
						}

						notiDtoList.add(notiDto);
					}
				}
			} else {
				notiList = notificationDao.findByNotisubject(pageRequest, params);
				if (notiList != null) {
					for (Notification notification : notiList) {
						NotificationDto notiDto = new NotificationDto();
						notiDto = modelMapper.map(notification, NotificationDto.class);
						if (notification.getModeNotiList() != null) {
							for (ModeNoti modeNoti : notification.getModeNotiList()) {
								modeNotiDto = modelMapper.map(modeNoti, ModeNotiDto.class);
								modeNotiDtoList.add(modeNotiDto);
							}
							notiDto.setModeNotiDto(modeNotiDtoList);
						}
						if (notification.getEmergencyList() != null) {
							for (Emergency emergency : notification.getEmergencyList()) {
								emergencyDto = modelMapper.map(emergency, EmergencyDto.class);
								emergencyDtoList.add(emergencyDto);
							}
							notiDto.setModeNotiDto(modeNotiDtoList);
						}
						if (notification.getRouteList() != null) {
							for (Route route : notification.getRouteList()) {
								routeDto = modelMapper.map(route, RouteDto.class);
								routeDtoList.add(routeDto);
							}
							notiDto.setRouteDto(routeDtoList);
						}

						notiDtoList.add(notiDto);
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

		return new PageImpl<>(notiDtoList, pageRequest, notiList.getTotalElements());
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
