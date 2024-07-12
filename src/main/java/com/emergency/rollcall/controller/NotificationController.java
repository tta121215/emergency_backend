package com.emergency.rollcall.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.rollcall.dto.EmergencyDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.NotificationDto;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.EmergencyService;
import com.emergency.rollcall.service.NotificationService;

@RestController
@CrossOrigin
@RequestMapping("notification")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@PostMapping("/save")
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

	@GetMapping("/notification")
	public ResponseEntity<Response<NotificationDto>> getById(@RequestParam("id") Long id) {
		NotificationDto notiDto = new NotificationDto();
		Response<NotificationDto> response = new Response<>();
		Message message = new Message();

		if (id != null) {
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

	@PostMapping("/update")
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

	@PostMapping("/delete")
	public ResponseEntity<Response<ResponseDto>> deleteAssebmly(@RequestBody NotificationDto notiDto) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		if (notiDto != null) {
			responseDto = notificationService.deleteNotification(notiDto);
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

	@GetMapping("/get-list")
	public ResponseEntity<ResponseList<NotificationDto>> emergencyList() {

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

}
