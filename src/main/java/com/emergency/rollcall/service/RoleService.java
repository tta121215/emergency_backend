package com.emergency.rollcall.service;

import java.util.List;

import org.springframework.data.domain.Page;
import com.emergency.rollcall.dto.RoleDto;
import com.emergency.rollcall.dto.ResponseDto;


public interface RoleService {

	ResponseDto saveRole(RoleDto roleDto);

	RoleDto getById(long id);

	ResponseDto updateRole(RoleDto roleDto);

	ResponseDto deleteRole(long id);

//	Page<RoleDto> searchByParams(int page, int size, String params, String sortBy, String direction);
//	
//	List<RoleDto> getAllList();

}
