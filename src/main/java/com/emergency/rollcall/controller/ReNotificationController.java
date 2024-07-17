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

	@Autowired
	private ReNotificationService reNotificationService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveLocEmergency(@RequestBody ReNotificationDto ReNotificationDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (ReNotificationDto != null) {
			responseDto = reNotificationService.saveReNotification(ReNotificationDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<ReNotificationDto>> getById(@RequestParam("id") Long id) {
		ReNotificationDto ReNotificationDto = new ReNotificationDto();
		Response<ReNotificationDto> response = new Response<>();
		Message message = new Message();

		if (id != null) {
			ReNotificationDto = reNotificationService.getById(id);
			if (ReNotificationDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
		}
		response.setMessage(message);
		response.setData(ReNotificationDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateLocEmergency(@RequestBody ReNotificationDto ReNotificationDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (ReNotificationDto != null) {
			responseDto = reNotificationService.updateReNotification(ReNotificationDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
			} else {
				message.setState(true);
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
	public ResponseEntity<Response<ResponseDto>> deleteLocEmergency(@PathParam ("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		if (id != 0) {
			responseDto = reNotificationService.deleteReNotification(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@GetMapping("")
	public ResponseEntity<ResponseList<ReNotificationDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,@RequestParam("params") String params) {

		ResponseList<ReNotificationDto> response = new ResponseList<>();
		Message message = new Message();
		List<ReNotificationDto> ReNotificationDtoList = new ArrayList<>();
		Page<ReNotificationDto> reNotiPage = reNotificationService.searchByParams(page,size,params);
		ReNotificationDtoList = reNotiPage.getContent();
		if (!ReNotificationDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
		}

		response.setMessage(message);
		response.setData(ReNotificationDtoList);
		response.setTotalItems(reNotiPage.getSize());
		response.setTotalPages(reNotiPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
