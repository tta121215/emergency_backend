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

import com.emergency.rollcall.dto.LocEmergencyDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.LocEmergencyService;

@RestController
@CrossOrigin
@RequestMapping("locemergency")
public class LocEmergencyController {
	
	private static final Logger logger = LoggerFactory.getLogger(LocEmergencyController.class);

	@Autowired
	private LocEmergencyService locEmergencyService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveLocEmergency(@RequestBody LocEmergencyDto locEmergencyDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save location of emergency with data: {}", locEmergencyDto);
		if (locEmergencyDto != null) {
			responseDto = locEmergencyService.saveLocEmergency(locEmergencyDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Succesfully to save location of emergency with data: {}", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save");
			logger.info("Error to save location of emergency with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<LocEmergencyDto>> getById(@PathVariable Long id) {
		LocEmergencyDto locEmergencyDto = new LocEmergencyDto();
		Response<LocEmergencyDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve location of emergency with data: {}", id);
		if (id != null) {
			locEmergencyDto = locEmergencyService.getById(id);
			if (locEmergencyDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully to retrieve location of emergency with data: {}", locEmergencyDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve location of emergency with data: {}", locEmergencyDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve location of emergency with data: {}", locEmergencyDto);
		}
		response.setMessage(message);
		response.setData(locEmergencyDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateLocEmergency(@RequestBody LocEmergencyDto locEmergencyDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update location of emergency with data: {}", locEmergencyDto);
		if (locEmergencyDto != null) {
			responseDto = locEmergencyService.updateLocEmergency(locEmergencyDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update location of emergency with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to save location of emergency with data: {}", responseDto);
			}
		} else {
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update location of emergency with data: {}", locEmergencyDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteLocEmergency(@PathVariable("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to delete location of emergency with data: {}", id);
		if (id != 0) {
			responseDto = locEmergencyService.deleteLocEmergency(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to delete location of emergency with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to delete location of emergency with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Data does not found location of emergency with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@GetMapping("")
	public ResponseEntity<ResponseList<LocEmergencyDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,@RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<LocEmergencyDto> response = new ResponseList<>();
		Message message = new Message();
		List<LocEmergencyDto> locEmergencyDtoList = new ArrayList<>();
		logger.info("Received request to search location of emergency with data: {}", page,size,params,sortBy,direction);
		Page<LocEmergencyDto> locEmergencyPage = locEmergencyService.searchByParams(page,size,params,sortBy,direction);
		locEmergencyDtoList = locEmergencyPage.getContent();
		if (!locEmergencyDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Succesfully location of emergency with data: {}", locEmergencyDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found location of emergency with data: {}", locEmergencyDtoList);
		}

		response.setMessage(message);
		response.setData(locEmergencyDtoList);
		response.setTotalItems(locEmergencyPage.getTotalElements());
		response.setTotalPages(locEmergencyPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@GetMapping("all-list")
	public ResponseEntity<ResponseList<LocEmergencyDto>> getAllList() {

		ResponseList<LocEmergencyDto> response = new ResponseList<>();
		Message message = new Message();
		List<LocEmergencyDto> locEmergencyDtoList = new ArrayList<>();
		logger.info("Received request to search location of emergency : ");
		locEmergencyDtoList= locEmergencyService.getAllList();
				
		if (!locEmergencyDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully location of emergency with data: {}", locEmergencyDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found location of emergency with data: {}", locEmergencyDtoList);
		}

		response.setMessage(message);
		response.setData(locEmergencyDtoList);		
		return new ResponseEntity<>(response, HttpStatus.OK);

	}


}
