package com.emergency.rollcall.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.rollcall.dto.ConditionDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.ConditionService;

@RestController
@CrossOrigin
@RequestMapping("condition")
public class ConditionController {
	
	private static final Logger logger = LoggerFactory.getLogger(AssemblyController.class);

	@Autowired
	private ConditionService conditionService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveCondition(@RequestBody ConditionDto conditionDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save condition with data: {}", conditionDto);
		if (conditionDto != null) {
			responseDto = conditionService.saveCondition(conditionDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully save condition ", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save condtion");
			logger.info("Error Save condtion", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<ConditionDto>> getById(@PathVariable Long id) {
		ConditionDto conditionDto = new ConditionDto();
		Response<ConditionDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve condition : ", id);

		if (id != null) {
			conditionDto = conditionService.getById(id);
			if (conditionDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully retrived condition with data: {}", conditionDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("No data found : ", conditionDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("No data found : ", conditionDto);
		}
		response.setMessage(message);
		response.setData(conditionDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateCondition(@RequestBody ConditionDto conditionDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update condition with data: {}", conditionDto);
		if (conditionDto != null) {
			responseDto = conditionService.updateCondition(conditionDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found condition with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfuly to update condition with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found condition with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteCondition(@PathVariable("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to delete condition with data: {}", id);
		if (id != 0) {
			responseDto = conditionService.deleteCondition(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update condition with data: {}", id);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully deleted condition with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Data does not found to update condition with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<ConditionDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<ConditionDto> response = new ResponseList<>();
		Message message = new Message();
		List<ConditionDto> conditionDtoList = new ArrayList<>();		
		logger.info("Received request to search condition with data: {}", page, size,params,sortBy,direction);
		Page<ConditionDto> conditionPage = conditionService.searchByParams(page, size, params, sortBy, direction);
		conditionDtoList = conditionPage.getContent();
		if (!conditionDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully retrieve to search condition with data: {}", conditionDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found condition with data: {}", conditionDtoList);
		}

		response.setMessage(message);
		response.setData(conditionDtoList);
		response.setTotalItems(conditionPage.getTotalElements());
		response.setTotalPages(conditionPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@GetMapping("all-list")
	public ResponseEntity<ResponseList<ConditionDto>> getAllList() {

		ResponseList<ConditionDto> response = new ResponseList<>();
		Message message = new Message();
		List<ConditionDto> conditionList = new ArrayList<>();
		logger.info("Received request all condition list : ");
		conditionList= conditionService.getAllList();		
		if (!conditionList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully condition list with data: {}", conditionList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found condition list : ", conditionList);
		}

		response.setMessage(message);
		response.setData(conditionList);		
		return new ResponseEntity<>(response, HttpStatus.OK);

	}


}
