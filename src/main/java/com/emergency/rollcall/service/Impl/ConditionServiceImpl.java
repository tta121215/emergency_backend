package com.emergency.rollcall.service.Impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.emergency.rollcall.dao.ConditionDao;
import com.emergency.rollcall.dto.ConditionDto;

import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.Condition;

import com.emergency.rollcall.service.ConditionService;

@Service
public class ConditionServiceImpl implements ConditionService {

	@Autowired
	private ConditionDao conditionDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveCondition(ConditionDto conditionDto) {
		ResponseDto res = new ResponseDto();
		Condition condition = new Condition();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		condition = modelMapper.map(conditionDto, Condition.class);

		condition.setCreateddate(this.yyyyMMddFormat(strCreatedDate));

		if (condition.getSyskey() == 0) {
			Condition entityres = conditionDao.save(condition);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Saved");
			}
		} else {
			Condition entityres = conditionDao.save(condition);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
			}
		}

		return res;
	}

	@Override
	public ConditionDto getById(long id) {
		ConditionDto conditionDto = new ConditionDto();
		Condition condition = new Condition();
		Optional<Condition> condtionOptional = conditionDao.findById(id);
		if (condtionOptional.isPresent()) {
			condition = condtionOptional.get();
			conditionDto = modelMapper.map(condition, ConditionDto.class);
		}
		return conditionDto;
	}

	@Override
	public ResponseDto updateCondition(ConditionDto conditionDto) {
		ResponseDto res = new ResponseDto();
		Condition condition = new Condition();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		Optional<Condition> conditionOptional = conditionDao.findById(conditionDto.getSyskey());
		if (conditionOptional.isPresent()) {

			condition = conditionOptional.get();
			condition = modelMapper.map(conditionDto, Condition.class);
			condition.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
			conditionDao.save(condition);
			res.setStatus_code(200);
			res.setMessage("Successfully Updated");
		} else {
			res.setStatus_code(401);
			res.setMessage("Data does not found");
		}

		return res;
	}

	@Override
	public ResponseDto deleteCondition(long id) {
		ResponseDto res = new ResponseDto();
		Condition condition = new Condition();

		Optional<Condition> conditionOptional = conditionDao.findById(id);
		if (conditionOptional.isPresent()) {
			condition = conditionOptional.get();
			conditionDao.delete(condition);
			res.setStatus_code(200);
			res.setMessage("Successfully Deleted");
		} else {
			res.setStatus_code(401);
			res.setMessage("No data found");
		}

		return res;
	}

	@Override
	public Page<ConditionDto> searchByParams(int page, int size, String params,String sortBy,String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
	    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));		
		Page<Condition> conditionList;
		List<ConditionDto> conditionDtoList = new ArrayList<>();
		if (params == null || params.isEmpty()) {
			conditionList = conditionDao.findByNameOrCode(pageRequest);
			if (conditionList != null) {
				for (Condition condition : conditionList) {
					ConditionDto conditionDto = new ConditionDto();
					conditionDto = modelMapper.map(condition, ConditionDto.class);
					conditionDtoList.add(conditionDto);
				}
			}

		} else {
			conditionList = conditionDao.findByNameOrCode(pageRequest, params);
			if (conditionList != null) {
				for (Condition condition : conditionList) {
					ConditionDto conditionDto = new ConditionDto();
					conditionDto = modelMapper.map(condition, ConditionDto.class);
					conditionDtoList.add(conditionDto);
				}
			}
		}

		return new PageImpl<>(conditionDtoList, pageRequest, conditionList.getTotalElements());
	}

	public String ddMMyyyFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null)
			l_Date = aDate.substring(6) + "-" + aDate.substring(4, 6) + "-" + aDate.substring(0, 4);

		return l_Date;
	}

	public String yyyyMMddFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null)
			l_Date = aDate.replaceAll("-", "");

		return l_Date;
	}
}
