package com.emergency.rollcall.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

	@Autowired
	private NotificationService notificationService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveNotification(@RequestBody NotificationDto notiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (notiDto != null) {
			responseDto = notificationService.saveNotification(notiDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
			}

		} else {
			message.setCode("401");
			message.setMessage("Error Save assembly");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<NotificationDto>> getById(@RequestParam("id") long id) {
		NotificationDto notiDto = new NotificationDto();
		Response<NotificationDto> response = new Response<>();
		Message message = new Message();

		if (id != 0) {
			notiDto = notificationService.getById(id);
			if (notiDto != null) {
				message.setCode("200");
				message.setMessage("Data is successfully");

			} else {
				message.setCode("401");
				message.setMessage("No Data found");
			}
		} else {
			message.setCode("401");
			message.setMessage("No Data found");
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
		if (notiDto != null) {
			responseDto = notificationService.updateNotification(notiDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
			} else {
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
			}
		} else {
			message.setCode("404");
			message.setMessage("Data does not dound");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteNotification(@PathParam ("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		if (id != 0) {
			responseDto = notificationService.deleteNotification(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
			} else {
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
			}
		} else {
			message.setCode("401");
			message.setMessage("No data found");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<NotificationDto>> notificationList() {

		ResponseList<NotificationDto> response = new ResponseList<>();
		Message message = new Message();
		List<NotificationDto> notificationDtoList = new ArrayList<>();
		notificationDtoList = notificationService.getAllList();

		if (!notificationDtoList.isEmpty()) {
			message.setCode("200");
			message.setMessage("Data is successfully");

		} else {
			message.setCode("401");
			message.setMessage("No Data found");
		}

		response.setMessage(message);
		response.setData(notificationDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@GetMapping("/search-by-params")
	public ResponseEntity<ResponseList<NotificationDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
			@RequestParam("params") String params) {

		ResponseList<NotificationDto> response = new ResponseList<>();
		Message message = new Message();
		List<NotificationDto> notificationDtoList = new ArrayList<>();
		Page<NotificationDto>  notiDto= notificationService.searchByParams(page, size,params);
		notificationDtoList = notiDto.getContent();
		if (!notificationDtoList.isEmpty()) {
			message.setCode("200");
			message.setMessage("Data is successfully");

		} else {
			message.setCode("401");
			message.setMessage("No Data found");
		}

		response.setMessage(message);
		response.setData(notificationDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
