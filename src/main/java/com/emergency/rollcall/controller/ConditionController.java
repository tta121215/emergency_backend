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

import com.emergency.rollcall.dto.ConditionDto;
import com.emergency.rollcall.dto.EmergencyDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.ConditionService;
import com.emergency.rollcall.service.EmergencyService;

@RestController
@CrossOrigin
@RequestMapping("condition")
public class ConditionController {

	@Autowired
	private ConditionService conditionService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveCondition(@RequestBody ConditionDto conditionDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (conditionDto != null) {
			responseDto = conditionService.saveCondition(conditionDto);
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
	public ResponseEntity<Response<ConditionDto>> getById(@RequestParam("id") Long id) {
		ConditionDto conditionDto = new ConditionDto();
		Response<ConditionDto> response = new Response<>();
		Message message = new Message();

		if (id != null) {
			conditionDto = conditionService.getById(id);
			if (conditionDto.getSyskey() != 0) {
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
		response.setData(conditionDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/update")
	public ResponseEntity<Response<ResponseDto>> updateCondition(@RequestBody ConditionDto conditionDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (conditionDto != null) {
			responseDto = conditionService.updateCondition(conditionDto);
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
	public ResponseEntity<Response<ResponseDto>> deleteAssebmly(@RequestBody ConditionDto conditionDto) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		if (conditionDto != null) {
			responseDto = conditionService.deleteCondition(conditionDto);
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

	@GetMapping("/condition-list")
	public ResponseEntity<ResponseList<ConditionDto>> conditionList() {

		ResponseList<ConditionDto> response = new ResponseList<>();
		Message message = new Message();
		List<ConditionDto> conditionDtoList = new ArrayList<>();
		conditionDtoList = conditionService.getAllList();

		if (!conditionDtoList.isEmpty()) {
			message.setCode("200");
			message.setMessage("Data is successfully");

		} else {
			message.setCode("401");
			message.setMessage("No Data found");
		}

		response.setMessage(message);
		response.setData(conditionDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
