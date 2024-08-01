package com.emergency.rollcall.service.Impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.emergency.rollcall.dao.AssemblyCheckInDao;
import com.emergency.rollcall.dao.AssemblyDao;
import com.emergency.rollcall.dto.AssemblyCheckInDto;
import com.emergency.rollcall.dto.AssemblyPointCheckInDto;
import com.emergency.rollcall.dto.DashboardResponseDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.AssemblyCheckIn;
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
	private ModelMapper modelMapper;


	@Override
	public DashboardResponseDto getCheckInCountsByAssemblyPoint(Long emergencyActivateId) {
		// TODO Auto-generated method stub
		List<Object[]> results = assemblyCheckInDao.findCheckInCountsByAssemblyPoint(emergencyActivateId);
        List<AssemblyPointCheckInDto> checkInCounts = results.stream()
                .map(result -> {
                    Long assemblyPointSyskey = (Long) result[0];
                    Long checkInCount = (Long) result[1];
                    String assemblyPointName = assemblyDao.findById(assemblyPointSyskey).get().getName();
                    return new AssemblyPointCheckInDto(assemblyPointName, checkInCount);
                })
                .collect(Collectors.toList());

        Long totalCheckInCount = checkInCounts.stream()
                .mapToLong(AssemblyPointCheckInDto::getCheckInCount)
                .sum();

        return new DashboardResponseDto(checkInCounts, totalCheckInCount);
	}

	

	public String yyyyMMddFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null) {
			l_Date = aDate.replaceAll("-", "");
		}
		return l_Date;
	}
}
