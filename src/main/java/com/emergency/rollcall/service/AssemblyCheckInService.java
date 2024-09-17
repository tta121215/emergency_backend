package com.emergency.rollcall.service;

import java.util.List;

import com.emergency.rollcall.dto.AssemblyCheckInDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface AssemblyCheckInService {

	ResponseDto saveAssemblyCheckIn(AssemblyCheckInDto data);

	List<AssemblyCheckInDto> getAllListByActivationId(Long id);
	
	ResponseDto updateAssemblyCheckIn(AssemblyCheckInDto data);
}
