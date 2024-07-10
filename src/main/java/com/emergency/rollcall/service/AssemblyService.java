package com.emergency.rollcall.service;

import com.emergency.rollcall.dto.AssemblyDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface AssemblyService {

	
	ResponseDto saveAssembly(AssemblyDto data);
	AssemblyDto getById(Long id);

}
