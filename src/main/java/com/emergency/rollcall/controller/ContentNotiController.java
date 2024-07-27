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

import com.emergency.rollcall.dto.ContentNotiDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.ContentNotiService;

@RestController
@CrossOrigin
@RequestMapping("contentnoti")
public class ContentNotiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ContentNotiController.class);

	@Autowired
	private ContentNotiService contentNotiService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveContentNoti(@RequestBody ContentNotiDto contentNotiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save content noti with data: {}", contentNotiDto);
		if (contentNotiDto != null) {
			responseDto = contentNotiService.saveContentNoti(contentNotiDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to save content noti with data: {}", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save");
			logger.info("Error to save content noti with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<ContentNotiDto>> getById(@PathVariable Long id) {
		ContentNotiDto contentNotiDto = new ContentNotiDto();
		Response<ContentNotiDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve content noti with data: {}", id);

		if (id != null) {
			contentNotiDto = contentNotiService.getById(id);
			if (contentNotiDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Received request to retrieve content noti with data: {}", contentNotiDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve content noti with data: {}", contentNotiDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve content noti with data: {}", contentNotiDto);
		}
		response.setMessage(message);
		response.setData(contentNotiDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateLocEmergency(@RequestBody ContentNotiDto contentNotiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update content noti with data: {}", contentNotiDto);
		if (contentNotiDto != null) {
			responseDto = contentNotiService.updateContentNoti(contentNotiDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update content noti with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to update content noti with data: {}", responseDto);
			}
		} else {
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update content noti with data: {}", responseDto);
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
		logger.info("Received request to delete content noti with data: {}", id);
		if (id != 0) {
			responseDto = contentNotiService.deleteContentNoti(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Received request to delete content noti with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Received request to delete content noti with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Received request to delete content noti with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<ContentNotiDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<ContentNotiDto> response = new ResponseList<>();
		Message message = new Message();
		List<ContentNotiDto> contentNotiDtoList = new ArrayList<>();
		logger.info("Received request to search content noti with data: {}", page,size,params,sortBy,direction);
		Page<ContentNotiDto> contentNotiPage = contentNotiService.searchByParams(page, size, params, sortBy, direction);
		contentNotiDtoList = contentNotiPage.getContent();
		if (!contentNotiDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search content noti with data: {}", contentNotiDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search content noti with data: {}", contentNotiDtoList);
		}

		response.setMessage(message);
		response.setData(contentNotiDtoList);
		response.setTotalItems(contentNotiPage.getTotalElements());
		response.setTotalPages(contentNotiPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@GetMapping("all-list")
	public ResponseEntity<ResponseList<ContentNotiDto>> getAllList() {

		ResponseList<ContentNotiDto> response = new ResponseList<>();
		Message message = new Message();
		List<ContentNotiDto> contentNotiDtoList = new ArrayList<>();
		logger.info("Received request to search content noti : ");
		contentNotiDtoList= contentNotiService.getAllList();
		
		if (!contentNotiDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search content noti with data: {}", contentNotiDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found content noti with data: {}", contentNotiDtoList);
		}

		response.setMessage(message);
		response.setData(contentNotiDtoList);		
		return new ResponseEntity<>(response, HttpStatus.OK);

	}


}
