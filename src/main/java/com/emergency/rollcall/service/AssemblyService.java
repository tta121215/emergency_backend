package com.emergency.rollcall.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.AssemblyDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface AssemblyService {

	
	ResponseDto saveAssembly(AssemblyDto data);
	AssemblyDto getById(Long id);
	ResponseDto updateAssembly(AssemblyDto data);
	ResponseDto deleteAssembly(long id);
	List<AssemblyDto> getAllList();
	Page<AssemblyDto> searchByParams(int page,int size,String params);

}
