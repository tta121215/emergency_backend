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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.NotiReadLogDto;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.NotiReadLogService;


@RestController
@CrossOrigin
@RequestMapping("notireadlog")
public class NotiReadLogController {
	
	private static final Logger logger = LoggerFactory.getLogger(NotiReadLogController.class);

	@Autowired
	private NotiReadLogService notiReadLogService;
	
	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveNotiReadLog(@RequestBody NotiReadLogDto notiReadLogDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save condition with data: {}", notiReadLogDto);
		if (notiReadLogDto != null) {
			responseDto = notiReadLogService.saveNotiReadLog(notiReadLogDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully save condition ", responseDto);
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save condtion");
			logger.info("Error Save condtion", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@GetMapping("")
	public ResponseEntity<ResponseList<NotiReadLogDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<NotiReadLogDto> response = new ResponseList<>();
		Message message = new Message();
		List<NotiReadLogDto> auditLogDtoList = new ArrayList<>();
		logger.info("Received request to search auditlog with data: {}", page,size,params,sortBy,direction);
		Page<NotiReadLogDto> auditLogPage = notiReadLogService.searchByParams(page, size, params, sortBy, direction);
		auditLogDtoList = auditLogPage.getContent();
		if (!auditLogDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search read noti log with data: {}", auditLogDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search read noti log with data: {}", auditLogDtoList);
		}

		response.setMessage(message);
		response.setData(auditLogDtoList);
		response.setTotalItems(auditLogPage.getTotalElements());
		response.setTotalPages(auditLogPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	

}
