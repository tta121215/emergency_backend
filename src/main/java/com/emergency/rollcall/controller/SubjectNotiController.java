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

import com.emergency.rollcall.dto.SubjectNotiDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.SubjectNotiService;

@RestController
@CrossOrigin
@RequestMapping("subjectnoti")
public class SubjectNotiController {

	private static final Logger logger = LoggerFactory.getLogger(SubjectNotiController.class);

	@Autowired
	private SubjectNotiService subjectNotiService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveSubjectNoti(@RequestBody SubjectNotiDto subjectNotiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save subject noti with data: {}", subjectNotiDto);
		if (subjectNotiDto != null) {
			responseDto = subjectNotiService.saveSubjectNoti(subjectNotiDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to save subject noti with data: {}", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save");
			logger.info("Error to save subject noti with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<SubjectNotiDto>> getById(@PathVariable Long id) {
		SubjectNotiDto modeNotiDto = new SubjectNotiDto();
		Response<SubjectNotiDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve subject noti with data: {}", id);

		if (id != null) {
			modeNotiDto = subjectNotiService.getById(id);
			if (modeNotiDto.getId() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Received request to retrieve subject noti with data: {}", modeNotiDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve subject noti with data: {}", modeNotiDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve subject noti with data: {}", modeNotiDto);
		}
		response.setMessage(message);
		response.setData(modeNotiDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateSubjectNoti(@RequestBody SubjectNotiDto subjectNotiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update subject noti with data: {}", subjectNotiDto);
		if (subjectNotiDto != null) {
			responseDto = subjectNotiService.updateSubjectNoti(subjectNotiDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update subject noti with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to update subject noti with data: {}", responseDto);
			}
		} else {
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update subject noti with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteSubjectNoti(@PathVariable("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to delete subject noti with data: {}", id);
		if (id != 0) {
			responseDto = subjectNotiService.deleteSubjectNoti(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Received request to delete subject noti with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Received request to delete subject noti with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Received request to delete subject noti with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<SubjectNotiDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<SubjectNotiDto> response = new ResponseList<>();
		Message message = new Message();
		List<SubjectNotiDto> subjectNotiDtoList = new ArrayList<>();
		logger.info("Received request to search subject noti with data: {}", page, size, params, sortBy, direction);
		Page<SubjectNotiDto> subjectNotiPage = subjectNotiService.searchByParams(page, size, params, sortBy, direction);
		subjectNotiDtoList = subjectNotiPage.getContent();
		if (!subjectNotiDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search subject noti with data: {}", subjectNotiDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search subject noti with data: {}", subjectNotiDtoList);
		}

		response.setMessage(message);
		response.setData(subjectNotiDtoList);
		response.setTotalItems(subjectNotiPage.getTotalElements());
		response.setTotalPages(subjectNotiPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("all-list")
	public ResponseEntity<ResponseList<SubjectNotiDto>> getAllList() {

		ResponseList<SubjectNotiDto> response = new ResponseList<>();
		Message message = new Message();
		List<SubjectNotiDto> modeNotiDtoList = new ArrayList<>();
		logger.info("Received request to search subject noti : ");
		modeNotiDtoList = subjectNotiService.getAllList();

		if (!modeNotiDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search subject noti with data: {}", modeNotiDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found subject noti with data: {}", modeNotiDtoList);
		}

		response.setMessage(message);
		response.setData(modeNotiDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
