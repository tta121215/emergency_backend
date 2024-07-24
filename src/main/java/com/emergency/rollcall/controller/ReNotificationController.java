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

import com.emergency.rollcall.dto.ReNotificationDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.ReNotificationService;

@RestController
@CrossOrigin
@RequestMapping("renoti")
public class ReNotificationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReNotificationController.class);

	@Autowired
	private ReNotificationService reNotificationService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveLocEmergency(@RequestBody ReNotificationDto reNotificationDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();		
		logger.info("Received request to save re-notification with data: {}", reNotificationDto);
		if (reNotificationDto != null) {
			responseDto = reNotificationService.saveReNotification(reNotificationDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Received request to save re-notification with data: {}", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error to save re-notification ");
			logger.info("Error to save re-notification with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<ReNotificationDto>> getById(@PathVariable Long id) {
		ReNotificationDto reNotificationDto = new ReNotificationDto();
		Response<ReNotificationDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve notification with data: {}", id);
		if (id != null) {
			reNotificationDto = reNotificationService.getById(id);
			if (reNotificationDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully to retrieve re-notification with data: {}", reNotificationDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve notification with data: {}", reNotificationDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retriece notification with data: {}", reNotificationDto);
		}
		response.setMessage(message);
		response.setData(reNotificationDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateRenotification(@RequestBody ReNotificationDto reNotificationDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update re-notification with data: {}", reNotificationDto);
		if (reNotificationDto != null) {
			responseDto = reNotificationService.updateReNotification(reNotificationDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update re-notification with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to save re-notification with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update re-notification with data: {}", responseDto);
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
		logger.info("Received request to delete re-notification with data: {}", id);
		if (id != 0) {
			responseDto = reNotificationService.deleteReNotification(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Received request to delete re-notification with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to delete re-notification with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Data does not found to delete notification with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<ReNotificationDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<ReNotificationDto> response = new ResponseList<>();
		Message message = new Message();
		List<ReNotificationDto> reNotificationDtoList = new ArrayList<>();
		logger.info("Received request to search re-notification with data: {}", page,size,params,sortBy,direction);
		Page<ReNotificationDto> reNotiPage = reNotificationService.searchByParams(page, size, params, sortBy,
				direction);
		reNotificationDtoList = reNotiPage.getContent();
		if (!reNotificationDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search re-notification with data: {}", reNotificationDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search re-notification with data: {}", reNotificationDtoList);
		}

		response.setMessage(message);
		response.setData(reNotificationDtoList);
		response.setTotalItems(reNotiPage.getTotalElements());
		response.setTotalPages(reNotiPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
