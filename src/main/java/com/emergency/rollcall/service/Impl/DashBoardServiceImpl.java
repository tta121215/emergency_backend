package com.emergency.rollcall.service.Impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
		List<AssemblyPointCheckInDto> checkInCounts = new ArrayList<>();
		List<Assembly> allAssemblies = assemblyDao.findAllByStatusAndIsDelete(1, 0);

		List<Object[]> results = assemblyCheckInDao.findCheckInCountsByAssemblyPoint(emergencyActivateId);
		Map<Long, Long> checkInCountMap = results.stream()
				.collect(Collectors.toMap(result -> (Long) result[0], result -> (Long) result[1]));

		List<Object[]> maxCheckInTimes = assemblyCheckInDao.findMaxCheckInTimesByAssemblyPoint(emergencyActivateId);
		Map<Long, LocalTime> maxCheckInTimeMap = maxCheckInTimes.stream()
				.collect(Collectors.toMap(result -> (Long) result[0],
						result -> LocalTime.parse((String) result[1], DateTimeFormatter.ofPattern("HH:mm:ss"))));

		EmergencyActivate emergencyActivate = emergencyActivateDao.findById(emergencyActivateId).orElse(null);
		if (emergencyActivate.getStartTime() != null) {
			LocalDateTime startTime = LocalDateTime.parse(emergencyActivate.getStartTime(), formatter);
			checkInCounts = allAssemblies.stream().map(assembly -> {
				Long assemblyPointId = assembly.getSyskey();

				LocalTime maxCheckInTime = maxCheckInTimeMap.getOrDefault(assemblyPointId, LocalTime.MIN);
				LocalDateTime maxCheckInDateTime = LocalDateTime.of(startTime.toLocalDate(), maxCheckInTime);

				Duration duration = Duration.between(startTime, maxCheckInDateTime);

				long hours = duration.toHours();
				long minutes = duration.toMinutes();
				long seconds = duration.toSecondsPart();
				String totalTimeTaken = String.format("%02d:%02d:%02d", hours, minutes, seconds);
				Long checkInCount = checkInCountMap.getOrDefault(assembly.getSyskey(), 0L);
				if (checkInCount == 0) {
					totalTimeTaken = "00:00:00";
				}
				return new AssemblyPointCheckInDto(assembly.getName(), checkInCount, assembly.getSyskey(),
						totalTimeTaken);
			}).collect(Collectors.toList());
		}

		List<Map<String, Object>> allUsers = assemblyCheckInDao.findAllUsers();

		List<Map<String, Object>> checkedInUsers = assemblyCheckInDao
				.findCheckedInUsersByEmergencyActivate(emergencyActivateId);

		long totalCheckInCount = (long) checkedInUsers.size();
		dashboardDto.setAverageTime("00: 00 : 00");
		dashboardDto.setTotalTime("00 : 00 : 00");

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
	public Page<DashboardDetailDto> getByActivateAndAssembly(Long activateId, Long assemblyId, int page, int size,
			String sortBy, String direction, String params) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortBy);
		if (sortBy.equals("name")) {
			sort = Sort.by(sortDirection, "name");
		} else if (sortBy.equals("username")) {
			sort = Sort.by(sortDirection, "username");
		} else if (sortBy.equals("staffid")) {
			sort = Sort.by(sortDirection, "staffid");
		} else if (sortBy.equals("mobileno")) {
			sort = Sort.by(sortDirection, "mobileno");
		} else if (sortBy.equals("assemblyname")) {
			sort = Sort.by(sortDirection, "a.name");
		} else {
			sort = Sort.by(sortDirection, "name");
		}
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();
		Page<Map<String, Object>> usersCheckedInPage;
		try {
			if (params == null || params.isEmpty()) {
				usersCheckedInPage = assemblyCheckInDao.findUsersCheckedInByEmergencyAndAssembly(activateId, assemblyId,
						pageRequest);
			} else {
				usersCheckedInPage = assemblyCheckInDao.findUsersCheckedInByEmergencyAndAssembly(activateId, assemblyId,
						pageRequest, params);
			}
			if (!usersCheckedInPage.isEmpty()) {
				dashboardDetailDtoList = usersCheckedInPage.stream().map(staff -> {
					DashboardDetailDto detailDto = new DashboardDetailDto();
					detailDto.setId((BigDecimal) staff.get("id"));
					detailDto.setUsername((String) staff.get("username"));
					detailDto.setEmail((String) staff.get("email"));
					detailDto.setMobileNo((String) staff.get("mobileno"));
					String icNumber = (String) staff.get("icnumber");
					String passportNumber = (String) staff.get("passportnumber");
					if (icNumber != null && !icNumber.isEmpty()) {
						detailDto.setIcnumber(icNumber);
					} else if (passportNumber != null && !passportNumber.isEmpty()) {
						detailDto.setIcnumber(passportNumber);
					} else {
						detailDto.setIcnumber(" "); // Use space or another default value
					}
					detailDto.setStaffId((String) staff.get("staffid"));
					detailDto.setName((String) staff.get("name"));
					detailDto.setCheckInDate((String) staff.get("currentdate"));
					detailDto.setCheckInTime((String) staff.get("currenttime"));
					detailDto.setAssemblyName((String) staff.get("assembly"));
					return detailDto;
				}).collect(Collectors.toList());
			}
			if ("icnumber".equalsIgnoreCase(sortBy)) {
				Comparator<DashboardDetailDto> comparator = Comparator.comparing(
						dto -> dto.getIcnumber().isEmpty() ? null : dto.getIcnumber(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}

				dashboardDetailDtoList.sort(comparator);
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(dashboardDetailDtoList, pageRequest, usersCheckedInPage.getTotalElements());

	}

	@Override
	public Page<DashboardDetailDto> getByActivateId(Long activateId, int page, int size, String sortBy,
			String direction, String params) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortBy);
		if (sortBy.equals("name")) {
			sort = Sort.by(sortDirection, "name");
		} else if (sortBy.equals("username")) {
			sort = Sort.by(sortDirection, "username");
		} else if (sortBy.equals("staffid")) {
			sort = Sort.by(sortDirection, "staffid");
		} else if (sortBy.equals("mobileno")) {
			sort = Sort.by(sortDirection, "mobileno");
		} else if (sortBy.equals("assemblyname")) {
			sort = Sort.by(sortDirection, "a.name");
		} else {
			sort = Sort.by(sortDirection, "name");
		}
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();
		Page<Map<String, Object>> usersCheckedInPage;
		try {
			if (params == null || params.isEmpty()) {
				usersCheckedInPage = assemblyCheckInDao.findUsersCheckedInByEmergencyActivate(activateId, pageRequest);
			} else {
				usersCheckedInPage = assemblyCheckInDao.findUsersCheckedInByEmergencyActivate(activateId, pageRequest,
						params);
			}
			if (!usersCheckedInPage.isEmpty()) {
				dashboardDetailDtoList = usersCheckedInPage.stream().map(staff -> {
					DashboardDetailDto detailDto = new DashboardDetailDto();
					detailDto.setId((BigDecimal) staff.get("id"));
					detailDto.setUsername((String) staff.get("username"));
					detailDto.setEmail((String) staff.get("email"));
					detailDto.setMobileNo((String) staff.get("mobileno"));
					String icNumber = (String) staff.get("icnumber");
					String passportNumber = (String) staff.get("passportnumber");
					if (icNumber != null && !icNumber.isEmpty()) {
						detailDto.setIcnumber(icNumber);
					} else if (passportNumber != null && !passportNumber.isEmpty()) {
						detailDto.setIcnumber(passportNumber);
					} else {
						detailDto.setIcnumber(" ");
					}
					detailDto.setStaffId((String) staff.get("staffid"));
					detailDto.setName((String) staff.get("name"));
					detailDto.setCheckInDate((String) staff.get("currentdate"));
					detailDto.setCheckInTime((String) staff.get("currenttime"));
					detailDto.setAssemblyName((String) staff.get("assembly"));
					return detailDto;
				}).collect(Collectors.toList());
			}
//			if ("icnumber".equalsIgnoreCase(sortBy)) {
//				dashboardDetailDtoList
//						.sort((dto1, dto2) -> sortDirection.isAscending() ? dto1.getIcnumber().compareTo(dto2.getIcnumber())
//								: dto2.getIcnumber().compareTo(dto1.getIcnumber()));
//			}
//			
			if ("icnumber".equalsIgnoreCase(sortBy)) {
				Comparator<DashboardDetailDto> comparator = Comparator.comparing(
						dto -> dto.getIcnumber().isEmpty() ? null : dto.getIcnumber(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}

				dashboardDetailDtoList.sort(comparator);
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(dashboardDetailDtoList, pageRequest, usersCheckedInPage.getTotalElements());

	}

	@Override
	public Page<StaffDto> getAllUnCheckInList(Long activateId, int page, int size, String sortBy, String direction,
			String params) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortBy);
		if (sortBy.equals("name")) {
			sort = Sort.by(sortDirection, "name");
		} else if (sortBy.equals("username")) {
			sort = Sort.by(sortDirection, "username");
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
			if (params == null || params.isEmpty()) {
				usersNotCheckedInPage = assemblyCheckInDao.findUsersNotCheckedInByEmergencyActivate(activateId,
						pageRequest);
			} else {
				usersNotCheckedInPage = assemblyCheckInDao.findUsersNotCheckedInByEmergencyActivate(activateId,
						pageRequest, params);
			}
			if (!usersNotCheckedInPage.isEmpty()) {
				staffDtoList = usersNotCheckedInPage.stream().map(staff -> {
					StaffDto staffDto = new StaffDto();
					staffDto.setId((BigDecimal) staff.get("id"));
					staffDto.setUsername((String) staff.get("username"));
					staffDto.setEmail((String) staff.get("email"));
					staffDto.setMobileNo((String) staff.get("mobileNo"));
					staffDto.setName((String) staff.get("name"));
					String icNumber = (String) staff.get("icnumber");
					String passportNumber = (String) staff.get("passportnumber");
					if (icNumber != null && !icNumber.isEmpty()) {
						staffDto.setIcnumber(icNumber);
					} else if (passportNumber != null && !passportNumber.isEmpty()) {
						staffDto.setIcnumber(passportNumber);
					} else {
						staffDto.setIcnumber(" ");
					}
					staffDto.setStaffId((String) staff.get("staffid"));
					return staffDto;
				}).collect(Collectors.toList());
			}
			if ("icnumber".equalsIgnoreCase(sortBy)) {
				Comparator<StaffDto> comparator = Comparator.comparing(
						dto -> dto.getIcnumber().isEmpty() ? null : dto.getIcnumber(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				staffDtoList.sort(comparator);
			}

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
