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

import com.emergency.rollcall.dto.RoleDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.RoleService;

@RestController
@CrossOrigin
@RequestMapping("role")
public class RoleController {
	
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveRole(@RequestBody RoleDto roleDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save role with data: {}", roleDto);
		if (roleDto != null) {
			responseDto = roleService.saveRole(roleDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to save role with data: {}", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save");
			logger.info("Error to save role with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<RoleDto>> getById(@PathVariable Long id) {
		RoleDto roleDto = new RoleDto();
		Response<RoleDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve role with data: {}", id);

		if (id != null) {
			roleDto = roleService.getById(id);
			if (roleDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Received request to retrieve role with data: {}", roleDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve role with data: {}", roleDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve role with data: {}", roleDto);
		}
		response.setMessage(message);
		response.setData(roleDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateLocEmergency(@RequestBody RoleDto modeNotiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update role with data: {}", modeNotiDto);
		if (modeNotiDto != null) {
			responseDto = roleService.updateRole(modeNotiDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update role with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to update role with data: {}", responseDto);
			}
		} else {
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update role with data: {}", responseDto);
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
		logger.info("Received request to delete role with data: {}", id);
		if (id != 0) {
			responseDto = roleService.deleteRole(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Received request to delete role with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Received request to delete role with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Received request to delete role with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
//
//	@GetMapping("")
//	public ResponseEntity<ResponseList<RoleDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
//			@RequestParam(defaultValue = "syskey") String sortBy,
//			@RequestParam(defaultValue = "asc") String direction) {
//
//		ResponseList<RoleDto> response = new ResponseList<>();
//		Message message = new Message();
//		List<RoleDto> modeNotiDtoList = new ArrayList<>();
//		logger.info("Received request to search role with data: {}", page,size,params,sortBy,direction);
//		Page<RoleDto> modeNotiPage = roleService.searchByParams(page, size, params, sortBy, direction);
//		modeNotiDtoList = modeNotiPage.getContent();
//		if (!modeNotiDtoList.isEmpty()) {
//			message.setState(true);
//			message.setCode("200");
//			message.setMessage("Data is successfully");
//			logger.info("Successfully to search role with data: {}", modeNotiDtoList);
//
//		} else {
//			message.setState(false);
//			message.setCode("401");
//			message.setMessage("No Data found");
//			logger.info("Data does not found to search role with data: {}", modeNotiDtoList);
//		}
//
//		response.setMessage(message);
//		response.setData(modeNotiDtoList);
//		response.setTotalItems(modeNotiPage.getTotalElements());
//		response.setTotalPages(modeNotiPage.getTotalPages());
//		response.setCurrentPage(page);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//
//	}
	
//	@GetMapping("all-list")
//	public ResponseEntity<ResponseList<RoleDto>> getAllList() {
//
//		ResponseList<RoleDto> response = new ResponseList<>();
//		Message message = new Message();
//		List<RoleDto> modeNotiDtoList = new ArrayList<>();
//		logger.info("Received request to search role : ");
//		modeNotiDtoList= roleService.getAllList();
//		
//		if (!modeNotiDtoList.isEmpty()) {
//			message.setState(true);
//			message.setCode("200");
//			message.setMessage("Data is successfully");
//			logger.info("Successfully to search role with data: {}", modeNotiDtoList);
//
//		} else {
//			message.setState(false);
//			message.setCode("401");
//			message.setMessage("No Data found");
//			logger.info("Data does not found role with data: {}", modeNotiDtoList);
//		}
//
//		response.setMessage(message);
//		response.setData(modeNotiDtoList);		
//		return new ResponseEntity<>(response, HttpStatus.OK);
//
//	}


}
