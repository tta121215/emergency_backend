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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.rollcall.dto.DashboardDetailDto;
import com.emergency.rollcall.dto.DashboardResponseDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.dto.StaffDto;
import com.emergency.rollcall.service.DashBoardService;

@RestController
@CrossOrigin
@RequestMapping("dashboard")
public class DashBoardController {

	private static final Logger logger = LoggerFactory.getLogger(DashBoardController.class);

	@Autowired
	private DashBoardService dashboardService;

	@GetMapping("/check-in-counts")
	public ResponseEntity<Response<DashboardResponseDto>> getCheckInCountsByAssemblyPoint(
			@RequestParam("id") Long emergencyActivateSyskey) {
		Response<DashboardResponseDto> response = new Response<>();
		Message message = new Message();
		DashboardResponseDto dashboardResponseDto = dashboardService
				.getCheckInCountsByAssemblyPoint(emergencyActivateSyskey);
		if (dashboardResponseDto != null) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully data check in count list " + dashboardResponseDto);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("No data check in count list " + dashboardResponseDto);
		}

		response.setMessage(message);
		response.setData(dashboardResponseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/byassemblyandactivate")
	public ResponseEntity<ResponseList<DashboardDetailDto>> getAllListByActivateAndAssembly(
			@RequestParam("activateId") Long activateId, @RequestParam("assemblyId") Long assemblyId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction,@RequestParam("params") String params) {
		ResponseList<DashboardDetailDto> response = new ResponseList<>();
		Message message = new Message();
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();
		logger.info("Received request to check in count by activation id and assembly id " + activateId + " : And " + assemblyId);

		Page<DashboardDetailDto> dashboardDetailPage = dashboardService.getByActivateAndAssembly(activateId, assemblyId,
				page, size,sortBy,direction,params);
		dashboardDetailDtoList = dashboardDetailPage.getContent();
		if (!dashboardDetailDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully data to check in count by activation id and assembly id  " + activateId + " : And " + assemblyId);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("No data found for check in count by activation id and assembly id  " + activateId + " : And " + assemblyId);
		}

		response.setMessage(message);
		response.setData(dashboardDetailDtoList);
		response.setTotalItems(dashboardDetailPage.getTotalElements());
		response.setTotalPages(dashboardDetailPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/byactivate")
	public ResponseEntity<ResponseList<DashboardDetailDto>> getistByActivationId(
			@RequestParam("activateId") Long activateId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction,@RequestParam("params") String params) {
		ResponseList<DashboardDetailDto> response = new ResponseList<>();
		Message message = new Message();
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();

		logger.info("Received request to get check in count by activation id " + activateId);

		Page<DashboardDetailDto> dashboardDetailPage = dashboardService.getByActivateId(activateId, page, size,sortBy,direction,params);
		dashboardDetailDtoList = dashboardDetailPage.getContent();
		if (!dashboardDetailDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully data for check in count by activation id" + activateId );

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("No data found for check in count by activation " + activateId);
		}

		response.setMessage(message);
		response.setData(dashboardDetailDtoList);
		response.setTotalItems(dashboardDetailPage.getTotalElements());
		response.setTotalPages(dashboardDetailPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/notcheckinlist")
	public ResponseEntity<ResponseList<StaffDto>> getAllNotCheckInList(@RequestParam("activateId") Long activateId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction,@RequestParam("params") String params) {
		ResponseList<StaffDto> response = new ResponseList<>();
		Message message = new Message();
		List<StaffDto> staffDtoList = new ArrayList<>();
		logger.info("Received request to get not check in count by activation id " + activateId);

		Page<StaffDto> staffDtoPage = dashboardService.getAllUnCheckInList(activateId, page, size,sortBy,direction,params);
		staffDtoList = staffDtoPage.getContent();
		if (!staffDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to get not check in count by activation id " + activateId);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("No data found to get not check in count by activation id " + activateId);
		}

		response.setMessage(message);
		response.setData(staffDtoList);
		response.setTotalItems(staffDtoPage.getTotalElements());
		response.setTotalPages(staffDtoPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/staffphoto")
	public ResponseEntity<Response<String>> getStaffPhoto(@RequestParam("staffno") String staffNo) {
		Response<String> response = new Response<>();
		Message message = new Message();
		String staffPhoto = null;
		logger.info("Received request to get photo by staff no " + staffNo);
		
		if (staffNo != null) {
			staffPhoto = dashboardService.getStaffPhoto(staffNo);
			if (staffPhoto != null) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully to retrieve staff photo with data: {}", staffPhoto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve staff photo with data: {}", staffPhoto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve staff photo with data: {}", staffPhoto);
		}
		response.setMessage(message);
		response.setData(staffPhoto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
