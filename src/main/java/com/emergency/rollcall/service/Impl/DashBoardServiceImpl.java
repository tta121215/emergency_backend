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
import org.springframework.data.domain.Sort;
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
		dashboardDto.setAverageTime("00: 00 : 00");
		dashboardDto.setTotalTime("00 : 00 : 00");
		EmergencyActivate emergencyActivate = emergencyActivateDao.findById(emergencyActivateId).orElse(null);
		if (emergencyActivate != null) {
			if (emergencyActivate.getActivateStatus() == 2) {
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
				// double averageTimePerCheckInDouble = totalCheckInCount > 0 ?
				// totalTimeInMinutes / totalCheckInCount : 0;
				// long secondsTotal = (long) (averageTimePerCheckInDouble * 60);
				// System.out.println(" Tot sec " + secondsTotal + " kh " +
				// averageTimePerCheckInDouble);
				Duration averageDuration = Duration.ofMinutes((long) averageTimePerCheckIn);

				long totalTimeInSeconds = totalTimeInMinutes * 60;

				long averageTimeInSeconds = totalCheckInCount > 0 ? totalTimeInSeconds / totalCheckInCount : 0;

				Duration averageDuration1 = Duration.ofSeconds(averageTimeInSeconds);

				long avghours = averageDuration1.toHours();
				long avgminutes = averageDuration1.toMinutesPart();
				long avgseconds = averageDuration1.toSecondsPart();

//				long avghours = averageDuration.toHours();
//				long avgminutes = averageDuration.toMinutesPart();
//				long avgseconds = averageDuration.toSecondsPart();
				System.out.println("Total min " + totalTimeInMinutes + " : check in" + totalCheckInCount + " Mi"
						+ emergencyActivate.getEndTime());
				System.out.println("Hr " + avghours + " : min " + avgminutes + " : second " + avgseconds + " time pr "
						+ averageTimeInSeconds);
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
		}

		long totalUnCheckInCount = allUsers.size() - checkedInUsers.size();

		dashboardDto.setCheckInCounts(checkInCounts);
		dashboardDto.setTotalCheckInCount(totalCheckInCount);
		dashboardDto.setTotalNotCheckInCount(totalUnCheckInCount);
		dashboardDto.setTotalHeadCount((long) allUsers.size());
		return dashboardDto;
	}

	@Override
	public Page<DashboardDetailDto> getByActivateAndAssembly(Long activateId, Long assemblyId, int page, int size, String sortBy, String direction) {
		// TODO Auto-generated method stub
		//PageRequest pageRequest = PageRequest.of(page, size);
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();
		Page<AssemblyCheckIn> assemblyCheckInList;
		try {
			if (sortBy.equals("name") || sortBy.equals("passportnumber") || sortBy.equals("icnumber") || sortBy.equals("type") 
					|| sortBy.equals("staffid") || sortBy.equals("department")) {				
				pageRequest = PageRequest.of(page, size);
				assemblyCheckInList = assemblyCheckInDao.getListByAssemblyAndActivate(activateId, assemblyId, pageRequest);
			} else {				
				pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
				assemblyCheckInList = assemblyCheckInDao.getListByAssemblyAndActivate(activateId, assemblyId, pageRequest);
			}
			//assemblyCheckInList = assemblyCheckInDao.getListByAssemblyAndActivate(activateId, assemblyId, pageRequest);
			if (!assemblyCheckInList.isEmpty()) {
				for (AssemblyCheckIn assemblyCheckIn : assemblyCheckInList) {
					DashboardDetailDto dashboardDetailDto = new DashboardDetailDto();
					dashboardDetailDto.setId(assemblyCheckIn.getStaffId());
					Map<String, Object> user = assemblyCheckInDao.findByUserId(assemblyCheckIn.getStaffId());
					if (user != null) {
						dashboardDetailDto.setUsername((String) user.get("username"));
						dashboardDetailDto.setEmail((String) user.get("email"));
						dashboardDetailDto.setIcnumber((String) user.get("icnumber"));
						dashboardDetailDto.setMobileNo((String) user.get("mobileno"));
						dashboardDetailDto.setName((String) user.get("name"));
						dashboardDetailDto.setPassportNumber((String) user.get("passportnumber"));
						dashboardDetailDto.setStaffId((String) user.get("staffid"));

					}
					dashboardDetailDto.setCheckInDate(assemblyCheckIn.getCurrentdate());
					dashboardDetailDto.setCheckInTime(assemblyCheckIn.getCurrenttime());
					dashboardDetailDto.setLattitude(assemblyCheckIn.getLatitude());
					dashboardDetailDto.setLongtitude(assemblyCheckIn.getLongtiude());
					dashboardDetailDto.setDeviceType(assemblyCheckIn.getDeviceType());
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
	public Page<DashboardDetailDto> getByActivateId(Long activateId, int page, int size, String sortBy,
			String direction) {
		// TODO Auto-generated method stub
		// PageRequest pageRequest = PageRequest.of(page, size);
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();
		Page<AssemblyCheckIn> assemblyCheckInList;
		try {
			if (sortBy.equals("name") || sortBy.equals("passportnumber") || sortBy.equals("icnumber") || sortBy.equals("type") 
					|| sortBy.equals("staffid") || sortBy.equals("department")) {				
				pageRequest = PageRequest.of(page, size);
				assemblyCheckInList = assemblyCheckInDao.getListByActivationId(activateId, pageRequest);
			} else {				
				pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
				assemblyCheckInList = assemblyCheckInDao.getListByActivationId(activateId, pageRequest);
			}
			
			if (!assemblyCheckInList.isEmpty()) {
				for (AssemblyCheckIn assemblyCheckIn : assemblyCheckInList) {
					DashboardDetailDto dashboardDetailDto = new DashboardDetailDto();
					dashboardDetailDto.setId(assemblyCheckIn.getStaffId());
					Map<String, Object> user = assemblyCheckInDao.findByUserId(assemblyCheckIn.getStaffId());
					if (user != null) {
						dashboardDetailDto.setUsername((String) user.get("username"));
						dashboardDetailDto.setEmail((String) user.get("email"));
						dashboardDetailDto.setIcnumber((String) user.get("icnumber"));
						dashboardDetailDto.setMobileNo((String) user.get("mobileno"));
						dashboardDetailDto.setName((String) user.get("name"));
						dashboardDetailDto.setPassportNumber((String) user.get("passportnumber"));
						dashboardDetailDto.setStaffId((String) user.get("staffid"));

					}
					dashboardDetailDto.setCheckInDate(assemblyCheckIn.getCurrentdate());
					dashboardDetailDto.setCheckInTime(assemblyCheckIn.getCurrenttime());
					dashboardDetailDto.setLattitude(assemblyCheckIn.getLatitude());
					dashboardDetailDto.setLongtitude(assemblyCheckIn.getLongtiude());
					dashboardDetailDto.setDeviceType(assemblyCheckIn.getDeviceType());
					Optional<Assembly> assemblyOptional = assemblyDao.findById(assemblyCheckIn.getAssemblyPoint());
					if (assemblyOptional.isPresent()) {
						Assembly assembly = assemblyOptional.get();
						dashboardDetailDto.setAssemblyName(assembly.getName());
					}
					dashboardDetailDtoList.add(dashboardDetailDto);
				}
			}
			if ("name".equalsIgnoreCase(sortBy)) {
				dashboardDetailDtoList
						.sort((dto1, dto2) -> sortDirection.isAscending() ? dto1.getName().compareTo(dto2.getName())
								: dto2.getName().compareTo(dto1.getName()));
			} else if ("passportnumber".equalsIgnoreCase(sortBy)) {
				dashboardDetailDtoList.sort((dto1, dto2) -> sortDirection.isAscending()
						? dto1.getPassportNumber().compareTo(dto2.getPassportNumber())
						: dto2.getPassportNumber().compareTo(dto1.getPassportNumber()));
			} else if ("icnumber".equalsIgnoreCase(sortBy)) {
				dashboardDetailDtoList.sort(
						(dto1, dto2) -> sortDirection.isAscending() ? dto1.getIcnumber().compareTo(dto2.getIcnumber())
								: dto2.getIcnumber().compareTo(dto1.getIcnumber()));
			} else if ("type".equalsIgnoreCase(sortBy)) {
				dashboardDetailDtoList.sort(
						(dto1, dto2) -> sortDirection.isAscending() ? dto1.getType().compareTo(dto2.getType())
								: dto2.getType().compareTo(dto1.getType()));
			} else if ("staffid".equalsIgnoreCase(sortBy)) {
				dashboardDetailDtoList.sort(
						(dto1, dto2) -> sortDirection.isAscending() ? dto1.getStaffId().compareTo(dto2.getStaffId())
								: dto2.getStaffId().compareTo(dto1.getStaffId()));
			} else if ("department".equalsIgnoreCase(sortBy)) {
				dashboardDetailDtoList.sort(
						(dto1, dto2) -> sortDirection.isAscending() ? dto1.getDepartment().compareTo(dto2.getDepartment())
								: dto2.getDepartment().compareTo(dto1.getDepartment()));
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
	public Page<StaffDto> getAllUnCheckInList(Long activateId, int page, int size, String sortBy, String direction) {
		// TODO Auto-generated method stub
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		//PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		
		Sort sort = Sort.by(sortDirection, sortBy);
		if (sortBy.equals("name")) {
			sort = Sort.by(sortDirection, "name");
		} else if (sortBy.equals("username")) {
			sort = Sort.by(sortDirection, "username");
		} else if (sortBy.equals("icnumber")) {
			sort = Sort.by(sortDirection, "icnumber");
		} else if (sortBy.equals("passportnumber")) {
			sort = Sort.by(sortDirection, "passportnumber");
		} else if (sortBy.equals("staffid")) {
			sort = Sort.by(sortDirection, "staffid");
		} else if (sortBy.equals("mobileno")) {
			sort = Sort.by(sortDirection, "mobileno");
		} else {
			sort = Sort.by(sortDirection, "name");
		}
		PageRequest pageRequest = PageRequest.of(page, size, sort);
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
