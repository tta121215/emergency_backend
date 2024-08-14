package com.emergency.rollcall.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.emergency.rollcall.dto.AssemblyCheckInDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.AssemblyCheckInService;

@RestController
@CrossOrigin
@RequestMapping("asscheckin")
public class AssemblyCheckInController {

	private static final Logger logger = LoggerFactory.getLogger(AssemblyCheckInController.class);

	@Autowired
	private AssemblyCheckInService assemblyCheckInService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveAssemblyCheckIn(
			@RequestBody AssemblyCheckInDto assemblyCheckInDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save assembly check in with data: {}", assemblyCheckInDto);
		if (assemblyCheckInDto != null) {
			responseDto = assemblyCheckInService.saveAssemblyCheckIn(assemblyCheckInDto);
			message.setState(true);
			message.setCode("200");
			message.setMessage("Successfully Check In");
			logger.info("Successfully saved assembly check in: {}", responseDto);
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save assembly check in");
			logger.info("Assembly check in saved error occured");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ResponseList<AssemblyCheckInDto>> getAllListByActivationId(@PathVariable Long id) {
		ResponseList<AssemblyCheckInDto> response = new ResponseList<>();
		Message message = new Message();
		List<AssemblyCheckInDto> AssemblyCheckInDtoList = new ArrayList<>();
		logger.info("Received request to get assembly check in by activation id " + id);
		
		
		if (id != null) {
			AssemblyCheckInDtoList = assemblyCheckInService.getAllListByActivationId(id);
			if (!AssemblyCheckInDtoList.isEmpty()) {
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
			logger.info("Assembly check in retrieve data error occured " + message.getMessage());
		}
		response.setMessage(message);
		response.setData(AssemblyCheckInDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}	
	
}
