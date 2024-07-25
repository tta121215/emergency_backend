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

import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.NotificationDto;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;

import com.emergency.rollcall.service.NotificationService;

@RestController
@CrossOrigin
@RequestMapping("noti")
public class NotificationController {

	private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	private NotificationService notificationService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveNotification(@RequestBody NotificationDto notiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save notification with data: {}", notiDto);
		if (notiDto != null) {
			responseDto = notificationService.saveNotification(notiDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to save notification with data: {}", notiDto);
			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Error to save notification with data: {}", notiDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error save notification");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<NotificationDto>> getById(@PathVariable long id) {
		NotificationDto notiDto = new NotificationDto();
		Response<NotificationDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve notification with data: {}", id);

		if (id != 0) {
			notiDto = notificationService.getById(id);
			if (notiDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully to save notification with data: {}", notiDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve notification with data: {}", notiDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve notification with data: {}", notiDto);
		}
		response.setMessage(message);
		response.setData(notiDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateNotification(@RequestBody NotificationDto notiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update notification with data: {}", notiDto);
		if (notiDto != null) {
			responseDto = notificationService.updateNotification(notiDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update notification with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to update notification with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update notification with data: {}", responseDto);
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
		logger.info("Received request to delete notification with data: {}", id);

		if (id != 0) {
			responseDto = notificationService.deleteNotification(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update notification with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to update notification with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Received request to update notification with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<NotificationDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<NotificationDto> response = new ResponseList<>();
		Message message = new Message();
		List<NotificationDto> notificationDtoList = new ArrayList<>();
		logger.info("Received request to search notification with data: {}", page, size, params, sortBy, direction);
		Page<NotificationDto> notiDtoPage = notificationService.searchByParams(page, size, params, sortBy, direction);
		notificationDtoList = notiDtoPage.getContent();
		if (!notificationDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search notification with data: {}", notificationDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search notification with data: {}", notificationDtoList);
		}

		response.setMessage(message);
		response.setData(notificationDtoList);
		response.setTotalItems(notiDtoPage.getTotalElements());
		response.setTotalPages(notiDtoPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/listbyemergency")
	public ResponseEntity<ResponseList<NotificationDto>> searchByParams(@RequestParam("id") Long id) {

		ResponseList<NotificationDto> response = new ResponseList<>();
		Message message = new Message();
		List<NotificationDto> notificationDtoList = new ArrayList<>();
		logger.info("Received request to search notification with data: {}", id);
		notificationDtoList = notificationService.searchByEmergency(id);
		
		if (!notificationDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search notification with data: {}", notificationDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search notification with data: {}", notificationDtoList);
		}

		response.setMessage(message);
		response.setData(notificationDtoList);		
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
