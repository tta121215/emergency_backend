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

	@Autowired
	private EmergencyService emergencyService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveEmergency(@RequestBody EmergencyDto emergencyDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (emergencyDto != null) {
			responseDto = emergencyService.saveEmergency(emergencyDto);
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
	public ResponseEntity<Response<EmergencyDto>> getById(@RequestParam("id") long id) {
		EmergencyDto emergencyDto = new EmergencyDto();
		Response<EmergencyDto> response = new Response<>();
		Message message = new Message();

		if (id != 0) {
			emergencyDto = emergencyService.getById(id);
			if (emergencyDto.getSyskey() != 0) {
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
		response.setData(emergencyDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateEmergency(@RequestBody EmergencyDto emergencyDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (emergencyDto != null) {
			responseDto = emergencyService.updateEmergency(emergencyDto);
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
	public ResponseEntity<Response<ResponseDto>> deleteEmergency(@PathParam("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		if (id != 0) {
			responseDto = emergencyService.deleteEmergency(id);
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
	public ResponseEntity<ResponseList<EmergencyDto>> emergencyList() {

		ResponseList<EmergencyDto> response = new ResponseList<>();
		Message message = new Message();
		List<EmergencyDto> emergencyDtoList = new ArrayList<>();
		emergencyDtoList = emergencyService.getAllList();

		if (!emergencyDtoList.isEmpty()) {
			message.setCode("200");
			message.setMessage("Data is successfully");

		} else {
			message.setCode("401");
			message.setMessage("No Data found");
		}

		response.setMessage(message);
		response.setData(emergencyDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/search-by-params")
	public ResponseEntity<ResponseList<EmergencyDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params) {

		ResponseList<EmergencyDto> response = new ResponseList<>();
		Message message = new Message();
		List<EmergencyDto> emergencyDtoList = new ArrayList<>();
		Page<EmergencyDto> emergencyPage = emergencyService.searchByParams(page, size, params);
		emergencyDtoList = emergencyPage.getContent();
		if (!emergencyDtoList.isEmpty()) {
			message.setCode("200");
			message.setMessage("Data is successfully");

		} else {
			message.setCode("401");
			message.setMessage("No Data found");
		}

		response.setMessage(message);
		response.setData(emergencyDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
