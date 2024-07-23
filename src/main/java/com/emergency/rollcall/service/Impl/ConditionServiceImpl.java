package com.emergency.rollcall.service.Impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import java.time.ZonedDateTime;


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

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		condition = modelMapper.map(conditionDto, Condition.class);

		condition.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
		try {
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
		} catch (DataAccessException e) {
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ConditionDto getById(long id) {
		ConditionDto conditionDto = new ConditionDto();
		Condition condition = new Condition();
		try {
			Optional<Condition> condtionOptional = conditionDao.findById(id);
			if (condtionOptional.isPresent()) {
				condition = condtionOptional.get();
				conditionDto = modelMapper.map(condition, ConditionDto.class);
			}
		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return conditionDto;
	}

	@Override
	public ResponseDto updateCondition(ConditionDto conditionDto) {
		ResponseDto res = new ResponseDto();
		Condition condition = new Condition();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		try {
			Optional<Condition> conditionOptional = conditionDao.findById(conditionDto.getSyskey());
			if (conditionOptional.isPresent()) {

				condition = conditionOptional.get();
				createdDate = condition.getCreateddate();
				condition = modelMapper.map(conditionDto, Condition.class);
				condition.setCreateddate(createdDate);
				condition.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				conditionDao.save(condition);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
			}

		} catch (DataAccessException e) {
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ResponseDto deleteCondition(long id) {
		ResponseDto res = new ResponseDto();
		Condition condition = new Condition();
		try {
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
		} catch (DataAccessException e) {
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public Page<ConditionDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<Condition> conditionList;
		List<ConditionDto> conditionDtoList = new ArrayList<>();
		try {
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
		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(conditionDtoList, pageRequest, conditionList.getTotalElements());
	}
	
	@Override
	public List<ConditionDto> getAllList() {

		List<ConditionDto> conditionDtoList = new ArrayList<>();
		List<Condition> conditionList = new ArrayList<>();
		try {
			conditionList = conditionDao.findAllByStatus(1);
			if (conditionList != null) {
				for (Condition condition : conditionList) {
					ConditionDto conditionDto = new ConditionDto();
					conditionDto = modelMapper.map(condition, ConditionDto.class);
					conditionDtoList.add(conditionDto);
				}
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return conditionDtoList;
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
