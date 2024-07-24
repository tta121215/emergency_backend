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

import com.emergency.rollcall.dto.ModeNotiDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.ModeNotiService;

@RestController
@CrossOrigin
@RequestMapping("modenoti")
public class ModeNotiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ModeNotiController.class);

	@Autowired
	private ModeNotiService modeNotiService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveModeNoti(@RequestBody ModeNotiDto modeNotiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save mode noti with data: {}", modeNotiDto);
		if (modeNotiDto != null) {
			responseDto = modeNotiService.saveModeNoti(modeNotiDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to save mode noti with data: {}", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save");
			logger.info("Error to save mode noti with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<ModeNotiDto>> getById(@PathVariable Long id) {
		ModeNotiDto modeNotiDto = new ModeNotiDto();
		Response<ModeNotiDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve mode noti with data: {}", id);

		if (id != null) {
			modeNotiDto = modeNotiService.getById(id);
			if (modeNotiDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Received request to retrieve mode noti with data: {}", modeNotiDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve mode noti with data: {}", modeNotiDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve mode noti with data: {}", modeNotiDto);
		}
		response.setMessage(message);
		response.setData(modeNotiDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateLocEmergency(@RequestBody ModeNotiDto modeNotiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update mode noti with data: {}", modeNotiDto);
		if (modeNotiDto != null) {
			responseDto = modeNotiService.updateModeNoti(modeNotiDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update mode noti with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to update mode noti with data: {}", responseDto);
			}
		} else {
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update mode noti with data: {}", responseDto);
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
		logger.info("Received request to delete mode noti with data: {}", id);
		if (id != 0) {
			responseDto = modeNotiService.deleteModeNoti(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Received request to delete mode noti with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Received request to delete mode noti with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Received request to delete mode noti with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<ModeNotiDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<ModeNotiDto> response = new ResponseList<>();
		Message message = new Message();
		List<ModeNotiDto> modeNotiDtoList = new ArrayList<>();
		logger.info("Received request to search mode noti with data: {}", page,size,params,sortBy,direction);
		Page<ModeNotiDto> modeNotiPage = modeNotiService.searchByParams(page, size, params, sortBy, direction);
		modeNotiDtoList = modeNotiPage.getContent();
		if (!modeNotiDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search mode noti with data: {}", modeNotiDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search mode noti with data: {}", modeNotiDtoList);
		}

		response.setMessage(message);
		response.setData(modeNotiDtoList);
		response.setTotalItems(modeNotiPage.getTotalElements());
		response.setTotalPages(modeNotiPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@GetMapping("all-list")
	public ResponseEntity<ResponseList<ModeNotiDto>> getAllList() {

		ResponseList<ModeNotiDto> response = new ResponseList<>();
		Message message = new Message();
		List<ModeNotiDto> modeNotiDtoList = new ArrayList<>();
		logger.info("Received request to search mode noti : ");
		modeNotiDtoList= modeNotiService.getAllList();
		
		if (!modeNotiDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search mode noti with data: {}", modeNotiDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found mode noti with data: {}", modeNotiDtoList);
		}

		response.setMessage(message);
		response.setData(modeNotiDtoList);		
		return new ResponseEntity<>(response, HttpStatus.OK);

	}


}
