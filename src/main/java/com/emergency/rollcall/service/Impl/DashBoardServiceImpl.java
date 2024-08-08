package com.emergency.rollcall.service.Impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.emergency.rollcall.dao.AssemblyCheckInDao;
import com.emergency.rollcall.dao.AssemblyDao;
import com.emergency.rollcall.dao.EmergencyActivateDao;
import com.emergency.rollcall.dto.AssemblyPointCheckInDto;
import com.emergency.rollcall.dto.DashboardDetailDto;
import com.emergency.rollcall.dto.DashboardResponseDto;
import com.emergency.rollcall.dto.StaffDto;
import com.emergency.rollcall.entity.Assembly;
import com.emergency.rollcall.entity.AssemblyCheckIn;
import com.emergency.rollcall.entity.EmergencyActivate;
import com.emergency.rollcall.service.DashBoardService;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	private final Logger logger = Logger.getLogger(DashBoardService.class.getName());

	@Autowired
	private AssemblyDao assemblyDao;

	@Autowired
	private AssemblyCheckInDao assemblyCheckInDao;

	@Autowired
	private EmergencyActivateDao emergencyActivateDao;

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public DashboardResponseDto getCheckInCountsByAssemblyPoint(Long emergencyActivateId) {
		// TODO Auto-generated method stub
		DashboardResponseDto dashboardDto = new DashboardResponseDto();
		List<Assembly> allAssemblies = assemblyDao.findAll();

		List<Object[]> results = assemblyCheckInDao.findCheckInCountsByAssemblyPoint(emergencyActivateId);
		Map<Long, Long> checkInCountMap = results.stream()
				.collect(Collectors.toMap(result -> (Long) result[0], result -> (Long) result[1]));

		List<AssemblyPointCheckInDto> checkInCounts = allAssemblies.stream().map(assembly -> {
			Long checkInCount = checkInCountMap.getOrDefault(assembly.getSyskey(), 0L);
			return new AssemblyPointCheckInDto(assembly.getName(), checkInCount, assembly.getSyskey());
		}).collect(Collectors.toList());

		List<Map<String, Object>> allUsers = assemblyCheckInDao.findAllUsers();

		List<Map<String, Object>> checkedInUsers = assemblyCheckInDao
				.findCheckedInUsersByEmergencyActivate(emergencyActivateId);

		long totalCheckInCount = (long) checkedInUsers.size();

		EmergencyActivate emergencyActivate = emergencyActivateDao.findById(emergencyActivateId).orElse(null);
		if (emergencyActivate != null) {			
			LocalDateTime startTime = LocalDateTime.parse(emergencyActivate.getStartTime(), formatter);
			LocalDateTime endTime = LocalDateTime.parse(emergencyActivate.getEndTime(), formatter);

			Duration duration = Duration.between(startTime, endTime);
			long totalTimeInMinutes = duration.toMinutes();
			long days = duration.toDays();
			long hours = duration.toHours() % 24;
			long minutes = duration.toMinutes() % 60;
			long seconds = duration.getSeconds() % 60;

			if (days > 0) {
				days = days * 24;
			}
			hours = hours + days;
			String hoursstr = "";
			if (hours < 10) {
				hoursstr = "0" + hours;
			} else {
				hoursstr = "" + hours;
			}
			String minutesstr = "";
			if (minutes < 10) {
				minutesstr = "0" + minutes;
			} else {
				minutesstr = "" + minutes;
			}
			String secondstr = "";
			if (seconds < 10) {
				secondstr = "0" + seconds;
			} else {
				secondstr = "" + seconds;
			}
			dashboardDto.setTotalTime(hoursstr + ":" + minutesstr + ":" + secondstr);
			long averageTimePerCheckIn = totalCheckInCount > 0 ? totalTimeInMinutes / totalCheckInCount : 0;
			Duration averageDuration = Duration.ofMinutes((long) averageTimePerCheckIn);
			
			long avghours = averageDuration.toHours();
			long avgminutes = averageDuration.toMinutesPart();
			long avgseconds = averageDuration.toSecondsPart();
			String avghoursstr = "";
			if (avghours < 10) {
				avghoursstr = "0" + avghours;
			} else {
				avghoursstr = "" + avghours;
			}
			String avgminutesstr = "";
			if (avgminutes < 10) {
				avgminutesstr = "0" + avgminutes;
			} else {
				avgminutesstr = "" + avgminutes;
			}
			String avgsecondstr = "";
			if (avgseconds < 10) {
				avgsecondstr = "0" + avgseconds;
			} else {
				avgsecondstr = "" + avgseconds;
			}
			dashboardDto.setAverageTime(avghoursstr + ":" + avgminutesstr + ":" + avgsecondstr);
		}

		long totalUnCheckInCount = allUsers.size() - checkedInUsers.size();

		dashboardDto.setCheckInCounts(checkInCounts);
		dashboardDto.setTotalCheckInCount(totalCheckInCount);
		dashboardDto.setTotalNotCheckInCount(totalUnCheckInCount);
		dashboardDto.setTotalHeadCount((long) allUsers.size());
		return dashboardDto;
	}

	@Override
	public Page<DashboardDetailDto> getByActivateAndAssembly(Long activateId, Long assemblyId, int page, int size) {
		// TODO Auto-generated method stub
		PageRequest pageRequest = PageRequest.of(page, size);
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();
		Page<AssemblyCheckIn> assemblyCheckInList;
		try {
			assemblyCheckInList = assemblyCheckInDao.getListByAssemblyAndActivate(activateId, assemblyId, pageRequest);
			if (!assemblyCheckInList.isEmpty()) {
				for (AssemblyCheckIn assemblyCheckIn : assemblyCheckInList) {
					DashboardDetailDto dashboardDetailDto = new DashboardDetailDto();
					dashboardDetailDto.setId(assemblyCheckIn.getStaffId());
					dashboardDetailDto.setUsername(" ");
					dashboardDetailDto.setEmail(" ");
					dashboardDetailDto.setCheckInDate(assemblyCheckIn.getCurrentdate());
					dashboardDetailDto.setCheckInTime(assemblyCheckIn.getCurrenttime());
					dashboardDetailDto.setLattitude(assemblyCheckIn.getLatitude());
					dashboardDetailDto.setLongtitude(assemblyCheckIn.getLongtiude());
					Optional<Assembly> assemblyOptional = assemblyDao.findById(assemblyCheckIn.getAssemblyPoint());
					if (assemblyOptional.isPresent()) {
						Assembly assembly = assemblyOptional.get();
						dashboardDetailDto.setAssemblyName(assembly.getName());
					}
					dashboardDetailDtoList.add(dashboardDetailDto);
				}
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(dashboardDetailDtoList, pageRequest, assemblyCheckInList.getTotalElements());
	}

	@Override
	public Page<DashboardDetailDto> getByActivateId(Long activateId, int page, int size) {
		// TODO Auto-generated method stub
		PageRequest pageRequest = PageRequest.of(page, size);
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();
		Page<AssemblyCheckIn> assemblyCheckInList;
		try {
			assemblyCheckInList = assemblyCheckInDao.getListByActivationId(activateId, pageRequest);
			if (!assemblyCheckInList.isEmpty()) {
				for (AssemblyCheckIn assemblyCheckIn : assemblyCheckInList) {
					DashboardDetailDto dashboardDetailDto = new DashboardDetailDto();
					dashboardDetailDto.setId(assemblyCheckIn.getStaffId());
					dashboardDetailDto.setUsername(" ");
					dashboardDetailDto.setEmail(" ");
					dashboardDetailDto.setCheckInDate(assemblyCheckIn.getCurrentdate());
					dashboardDetailDto.setCheckInTime(assemblyCheckIn.getCurrenttime());
					dashboardDetailDto.setLattitude(assemblyCheckIn.getLatitude());
					dashboardDetailDto.setLongtitude(assemblyCheckIn.getLongtiude());
					Optional<Assembly> assemblyOptional = assemblyDao.findById(assemblyCheckIn.getAssemblyPoint());
					if (assemblyOptional.isPresent()) {
						Assembly assembly = assemblyOptional.get();
						dashboardDetailDto.setAssemblyName(assembly.getName());
					}
					dashboardDetailDtoList.add(dashboardDetailDto);
				}
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(dashboardDetailDtoList, pageRequest, assemblyCheckInList.getTotalElements());

	}

	@Override
	public Page<StaffDto> getAllUnCheckInList(Long activateId, int page, int size) {
		// TODO Auto-generated method stub
		PageRequest pageRequest = PageRequest.of(page, size);
		List<StaffDto> staffDtoList = new ArrayList<>();
		Page<Map<String, Object>> usersNotCheckedInPage;

		try {
			usersNotCheckedInPage = assemblyCheckInDao.findUsersNotCheckedInByEmergencyActivate(activateId,
					pageRequest);

			staffDtoList = usersNotCheckedInPage.stream().map(staff -> {
				StaffDto staffDto = new StaffDto();
				staffDto.setId((BigDecimal) staff.get("id"));
				staffDto.setUsername((String) staff.get("username"));
				staffDto.setEmail((String) staff.get("email"));
				staffDto.setMobileNo((String) staff.get("mobileNo"));
				staffDto.setName((String) staff.get("name"));
				staffDto.setIcnumber((String) staff.get("icnumber"));
				staffDto.setPassportNumber((String) staff.get("passportnumber"));
				staffDto.setStaffId((String) staff.get("staffid"));
				return staffDto;
			}).collect(Collectors.toList());

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(staffDtoList, pageRequest, usersNotCheckedInPage.getTotalElements());
	}

	public String yyyyMMddFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null) {
			l_Date = aDate.replaceAll("-", "");
		}
		return l_Date;
	}

}
