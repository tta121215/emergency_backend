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

	@GetMapping("")
	public ResponseEntity<Response<EmergencyDto>> getById(@RequestParam("id") Long id) {
		EmergencyDto emergencyDto = new EmergencyDto();
		Response<EmergencyDto> response = new Response<>();
		Message message = new Message();

		if (id != null) {
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

	@PostMapping("/update")
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

	@PostMapping("/delete")
	public ResponseEntity<Response<ResponseDto>> deleteAssebmly(@RequestBody EmergencyDto emergencyDto) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		if (emergencyDto != null) {
			responseDto = emergencyService.deleteEmergency(emergencyDto);
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

	@GetMapping("/emergency-list")
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

}
