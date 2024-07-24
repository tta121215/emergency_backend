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

import com.emergency.rollcall.dto.EmergencyActivateDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.EmergencyActivateService;


@RestController
@CrossOrigin
@RequestMapping("eactivate")
public class EmergencyActivateController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmergencyActivateController.class);

	@Autowired
	private EmergencyActivateService emergencyActivateService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveEmergencyActivate(@RequestBody EmergencyActivateDto eActivateDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save emergency activate with data: {}", eActivateDto);
		if (eActivateDto != null) {
			responseDto = emergencyActivateService.saveEmergencyActivate(eActivateDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully request to save emergency activate with data: {}", eActivateDto);
			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Error save emergency activate with data: {}", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save assembly");
			logger.info("Error save emergency activate with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<EmergencyActivateDto>> getById(@PathVariable long id) {
		EmergencyActivateDto eActivateDto = new EmergencyActivateDto();
		Response<EmergencyActivateDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve emergency with data: {}", id);
		if (id != 0) {
			eActivateDto = emergencyActivateService.getById(id);
			if (eActivateDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully retrieve emergency activate with data: {}", eActivateDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found emergency activate with data: {}", eActivateDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found emergency activate with data: {}", eActivateDto);
		}
		response.setMessage(message);
		response.setData(eActivateDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateNotification(@RequestBody EmergencyActivateDto eActivateDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update emergency with data: {}", eActivateDto);
		if (eActivateDto != null) {
			responseDto = emergencyActivateService.updateEmergencyActivate(eActivateDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found update emergency activate with data: {}", eActivateDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully update emergency activate with data: {}", eActivateDto);
			}
		} else {
			message.setState(false);
			message.setCode("404");
			message.setMessage("Data does not dound");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteNotification(@PathVariable("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to delete emergency activate with data: {}", id);
		if (id != 0) {
			responseDto = emergencyActivateService.deleteEmergencyActivate(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to delete emergency activate with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to delete emergency activate with data: {}", id);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Data does not found to delete emergency activate with data: {}", id);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<EmergencyActivateDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<EmergencyActivateDto> response = new ResponseList<>();
		Message message = new Message();
		logger.info("Received request to search emergency activate with data: {}", page,size,params,sortBy,direction);
		List<EmergencyActivateDto> emergencyActivateDtoList = new ArrayList<>();
		Page<EmergencyActivateDto> emergencyActivatePage = emergencyActivateService.searchByParams(page, size, params, sortBy, direction);
		emergencyActivateDtoList = emergencyActivatePage.getContent();
		if (!emergencyActivateDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search emergency activate with data: {}", emergencyActivateDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found emergency activate with data: {}", emergencyActivateDtoList);
		}

		response.setMessage(message);
		response.setData(emergencyActivateDtoList);
		response.setTotalItems(emergencyActivatePage.getTotalElements());
		response.setTotalPages(emergencyActivatePage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
