package com.emergency.rollcall.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.ConditionDto;

import com.emergency.rollcall.dto.ResponseDto;

public interface ConditionService {

	ResponseDto saveCondition(ConditionDto conditionDto);

	ConditionDto getById(long id);

	ResponseDto updateCondition(ConditionDto conditionDto);

	ResponseDto deleteCondition(long id);

	Page<ConditionDto> searchByParams(int page, int size, String params,String sortBy,String direction);
	
	List<ConditionDto> getAllList();

}
