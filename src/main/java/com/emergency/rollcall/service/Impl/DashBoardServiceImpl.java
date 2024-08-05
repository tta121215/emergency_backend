package com.emergency.rollcall.service.Impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;

import com.emergency.rollcall.dao.AssemblyCheckInDao;
import com.emergency.rollcall.dao.AssemblyDao;
import com.emergency.rollcall.dao.EmergencyActivateDao;
import com.emergency.rollcall.dto.AssemblyCheckInDto;
import com.emergency.rollcall.dto.AssemblyPointCheckInDto;
import com.emergency.rollcall.dto.DashboardResponseDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.Assembly;
import com.emergency.rollcall.entity.AssemblyCheckIn;
import com.emergency.rollcall.entity.EmergencyActivate;
import com.emergency.rollcall.service.AssemblyCheckInService;
import com.emergency.rollcall.service.DashBoardService;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	private final Logger logger = Logger.getLogger(AssemblyCheckInService.class.getName());

	@Autowired
	private AssemblyDao assemblyDao;

	@Autowired
	private AssemblyCheckInDao assemblyCheckInDao;

	@Autowired
	private EmergencyActivateDao emergencyActivateDao;
	
	@Autowired
	private ModelMapper modelMapper;

	

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
			return new AssemblyPointCheckInDto(assembly.getName(), checkInCount,assembly.getSyskey());
		}).collect(Collectors.toList());

		Long totalCheckInCount = checkInCounts.stream().mapToLong(AssemblyPointCheckInDto::getCheckInCount).sum();

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
				dashboardDto.setTotalTime(days + " days " + hours + " hours");
			} else if (hours > 0) {
				dashboardDto.setTotalTime(hours + " hours " + minutes + " minutes");
			} else {
				dashboardDto.setTotalTime(minutes + " minutes " + seconds + " seconds");
			}
			double averageTimePerCheckIn = totalCheckInCount > 0 ? (double) totalTimeInMinutes / totalCheckInCount : 0;
			dashboardDto.setAverageTime(averageTimePerCheckIn);
		}

		dashboardDto.setCheckInCounts(checkInCounts);
		dashboardDto.setTotalCheckInCount(totalCheckInCount);

		return dashboardDto;
	}
	
	@Override
	public Page<AssemblyCheckInDto> getByActivateAndAssembly(Long activateId, Long assemblyId, int page ,int size) {
		// TODO Auto-generated method stub
		PageRequest pageRequest = PageRequest.of(page, size);
		List<AssemblyCheckInDto> assemblyCheckInDtoList = new ArrayList<>();
		Page<AssemblyCheckIn> assemblyCheckInList; 
		try {
			assemblyCheckInList = assemblyCheckInDao.getListByAssemblyAndActivate(activateId, assemblyId, pageRequest);
			if (!assemblyCheckInList.isEmpty()) {
				for (AssemblyCheckIn assemblyCheckIn : assemblyCheckInList) {
					AssemblyCheckInDto assemblyCheckInDto = new AssemblyCheckInDto();
					assemblyCheckInDto = modelMapper.map(assemblyCheckIn, AssemblyCheckInDto.class);
					assemblyCheckInDtoList.add(assemblyCheckInDto);
				}
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(assemblyCheckInDtoList, pageRequest, assemblyCheckInList.getTotalElements());
	}
	
	@Override
	public Page<AssemblyCheckInDto> getByActivateId(Long activateId, int page, int size) {
		// TODO Auto-generated method stub
		PageRequest pageRequest = PageRequest.of(page, size);
		List<AssemblyCheckInDto> assemblyCheckInDtoList = new ArrayList<>();
		Page<AssemblyCheckIn> assemblyCheckInList;
		try {
			assemblyCheckInList = assemblyCheckInDao.getListByActivationId(activateId, pageRequest);
			if (!assemblyCheckInList.isEmpty()) {
				for (AssemblyCheckIn assemblyCheckIn : assemblyCheckInList) {
					AssemblyCheckInDto assemblyCheckInDto = new AssemblyCheckInDto();
					assemblyCheckInDto = modelMapper.map(assemblyCheckIn, AssemblyCheckInDto.class);
					assemblyCheckInDtoList.add(assemblyCheckInDto);
				}
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(assemblyCheckInDtoList, pageRequest, assemblyCheckInList.getTotalElements());
 
	}

	public String yyyyMMddFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null) {
			l_Date = aDate.replaceAll("-", "");
		}
		return l_Date;
	}

	

	
	
}
