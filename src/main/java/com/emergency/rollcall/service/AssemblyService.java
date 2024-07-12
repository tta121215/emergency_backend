package com.emergency.rollcall.service;

import java.util.List;

import com.emergency.rollcall.dto.AssemblyDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface AssemblyService {

	
	ResponseDto saveAssembly(AssemblyDto data);
	AssemblyDto getById(Long id);
	ResponseDto updateAssembly(AssemblyDto data);
	ResponseDto deleteAssembly(AssemblyDto data);
	List<AssemblyDto> getAllList();

}
