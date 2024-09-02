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

import com.emergency.rollcall.dto.MainBuildingDto;
import com.emergency.rollcall.dto.LocationDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.MainBuildingService;

@RestController
@CrossOrigin
@RequestMapping("mainbuilding")
public class MainBuildingController {

	private static final Logger logger = LoggerFactory.getLogger(MainBuildingController.class);

	@Autowired
	private MainBuildingService mainBuildingService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveMainBuilding(@RequestBody MainBuildingDto mainBuildingDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save main building with data: {}", mainBuildingDto);
		if (mainBuildingDto != null) {
			responseDto = mainBuildingService.saveMainBuilding(mainBuildingDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Succesfully to save main building with data: {}", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save");
			logger.info("Error to save main building with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<MainBuildingDto>> getById(@PathVariable Long id) {
		MainBuildingDto mainBuildingDto = new MainBuildingDto();
		Response<MainBuildingDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve main building with data: {}", id);
		if (id != null) {
			mainBuildingDto = mainBuildingService.getById(id);
			if (mainBuildingDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully to retrieve main building with data: {}", mainBuildingDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve main building with data: {}", mainBuildingDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve main building with data: {}", mainBuildingDto);
		}
		response.setMessage(message);
		response.setData(mainBuildingDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateMainBuilding(@RequestBody MainBuildingDto mainBuildingDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update main building with data: {}", mainBuildingDto);
		if (mainBuildingDto != null) {
			responseDto = mainBuildingService.updateMainBuilding(mainBuildingDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update main building with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to save main building with data: {}", responseDto);
			}
		} else {
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update main building with data: {}", mainBuildingDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteMainBuilding(@PathVariable("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to delete main building with data: {}", id);
		if (id != 0) {
			responseDto = mainBuildingService.deleteMainBuilding(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to delete main building with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to delete main building with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Data does not found main building with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<MainBuildingDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<MainBuildingDto> response = new ResponseList<>();
		Message message = new Message();
		List<MainBuildingDto> mainBuildingDtoList = new ArrayList<>();
		logger.info("Received request to search main building with data: {}", page, size, params, sortBy, direction);
		Page<MainBuildingDto> mainBuildingPage = mainBuildingService.searchByParams(page, size, params, sortBy,
				direction);
		mainBuildingDtoList = mainBuildingPage.getContent();
		if (!mainBuildingDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Succesfully main building with data: {}", mainBuildingDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found main building with data: {}", mainBuildingDtoList);
		}

		response.setMessage(message);
		response.setData(mainBuildingDtoList);
		response.setTotalItems(mainBuildingPage.getTotalElements());
		response.setTotalPages(mainBuildingPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("all-list")
	public ResponseEntity<ResponseList<MainBuildingDto>> getAllList() {

		ResponseList<MainBuildingDto> response = new ResponseList<>();
		Message message = new Message();
		List<MainBuildingDto> mainBuildingDtoList = new ArrayList<>();
		logger.info("Received request to search main building : ");
		mainBuildingDtoList = mainBuildingService.getAllList();

		if (!mainBuildingDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully main building with data: {}", mainBuildingDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found main building with data: {}", mainBuildingDtoList);
		}

		response.setMessage(message);
		response.setData(mainBuildingDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
