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

import com.emergency.rollcall.dto.EmergencyDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.EmergencyService;

@RestController
@CrossOrigin
@RequestMapping("emergency")
public class EmergencyController {

	private static final Logger logger = LoggerFactory.getLogger(EmergencyController.class);
	
	@Autowired
	private EmergencyService emergencyService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveEmergency(@RequestBody EmergencyDto emergencyDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save emergency with data: {}", emergencyDto);
		if (emergencyDto != null) {
			responseDto = emergencyService.saveEmergency(emergencyDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to save emergency with data: {}", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save assembly");
			logger.info("Error to save emergency with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<EmergencyDto>> getById(@PathVariable long id) {
		EmergencyDto emergencyDto = new EmergencyDto();
		Response<EmergencyDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve emergency with data: {}", id);
		if (id != 0) {
			emergencyDto = emergencyService.getById(id);
			if (emergencyDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully retriecve emergency with data: {}", emergencyDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found emergency with data: {}", emergencyDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found emergency with data: {}", emergencyDto);
		}
		response.setMessage(message);
		response.setData(emergencyDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateEmergency(@RequestBody EmergencyDto emergencyDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update emergency with data: {}", emergencyDto);
		if (emergencyDto != null) {
			responseDto = emergencyService.updateEmergency(emergencyDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update emergency with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to update emergency with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update emergency with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteEmergency(@PathVariable("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to delete emergency with data: {}", id);
		if (id != 0) {
			responseDto = emergencyService.deleteEmergency(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update emergency with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to delete emergency with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Data does not found to delete emergency with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<EmergencyDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<EmergencyDto> response = new ResponseList<>();
		Message message = new Message();
		List<EmergencyDto> emergencyDtoList = new ArrayList<>();
		logger.info("Received request to search emergency with data: {}", page,size,params,sortBy,direction);
		Page<EmergencyDto> emergencyPage = emergencyService.searchByParams(page, size, params,sortBy,direction);
		emergencyDtoList = emergencyPage.getContent();
		if (!emergencyDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search emergency with data: {}", emergencyDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search emergency with data: {}", emergencyDtoList);
		}

		response.setMessage(message);
		response.setData(emergencyDtoList);
		response.setTotalItems(emergencyPage.getTotalElements());
		response.setTotalPages(emergencyPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@GetMapping("all-list")
	public ResponseEntity<ResponseList<EmergencyDto>> getAllList() {

		ResponseList<EmergencyDto> response = new ResponseList<>();
		Message message = new Message();
		List<EmergencyDto> emergencyDtoList = new ArrayList<>();
		logger.info("Received request to retrieve emergency ");
		emergencyDtoList= emergencyService.getAllList();
		
		if (!emergencyDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search emergency : ", emergencyDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search emergency : ", emergencyDtoList);
		}

		response.setMessage(message);
		response.setData(emergencyDtoList);		
		return new ResponseEntity<>(response, HttpStatus.OK);

	}


}
