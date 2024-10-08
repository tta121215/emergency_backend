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
import com.emergency.rollcall.dto.AuditLogDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.AuditLogService;
import com.emergency.rollcall.service.RoleService;

@RestController
@CrossOrigin
@RequestMapping("auditlog")
public class AudtiLogController {
	
	private static final Logger logger = LoggerFactory.getLogger(AudtiLogController.class);

	@Autowired
	private AuditLogService auditLogSerivce;
	
	@GetMapping("")
	public ResponseEntity<ResponseList<AuditLogDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<AuditLogDto> response = new ResponseList<>();
		Message message = new Message();
		List<AuditLogDto> auditLogDtoList = new ArrayList<>();
		logger.info("Received request to search auditlog with data: {}", page,size,params,sortBy,direction);
		Page<AuditLogDto> auditLogPage = auditLogSerivce.searchByParams(page, size, params, sortBy, direction);
		auditLogDtoList = auditLogPage.getContent();
		if (!auditLogDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search role with data: {}", auditLogDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search role with data: {}", auditLogDtoList);
		}

		response.setMessage(message);
		response.setData(auditLogDtoList);
		response.setTotalItems(auditLogPage.getTotalElements());
		response.setTotalPages(auditLogPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	

}
