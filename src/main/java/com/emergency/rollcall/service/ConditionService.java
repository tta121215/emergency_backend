package com.emergency.rollcall.service;

import java.util.List;

import com.emergency.rollcall.dto.ConditionDto;

import com.emergency.rollcall.dto.ResponseDto;

public interface ConditionService {

	ResponseDto saveCondition(ConditionDto conditionDto);

	ConditionDto getById(Long id);

	ResponseDto updateCondition(ConditionDto conditionDto);

	ResponseDto deleteCondition(ConditionDto conditionDto);

	List<ConditionDto> getAllList();

}
