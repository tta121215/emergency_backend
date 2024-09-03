package com.emergency.rollcall.controller;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.emergency.rollcall.dto.DashboardDetailDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.NotiReadLogDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.dto.StaffDto;
import com.emergency.rollcall.service.ReportService;

@RestController
@CrossOrigin
@RequestMapping("report")
public class ReportController {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;

	@GetMapping("/checkinlist")
	public ResponseEntity<ResponseList<DashboardDetailDto>> getAllCheckInList(
			@RequestParam("activateId") Long activateId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction,@RequestParam("params") String params) {
		ResponseList<DashboardDetailDto> response = new ResponseList<>();
		Message message = new Message();
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();

		logger.info("Received request to get check in count by activation id " + activateId);

		Page<DashboardDetailDto> dashboardDetailPage = reportService.getAllCheckInList(activateId, page, size,sortBy,direction,params);
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

		Page<StaffDto> staffDtoPage = reportService.getAllUnCheckInList(activateId, page, size,sortBy,direction,params);
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

	@GetMapping("/notcheckin/excel")
	public ResponseEntity<byte[]> getAllNotCheckInList(@RequestParam("activateId") Long activateId) {
		
		
		logger.info("Received request to get not check in count by activation id " + activateId);
		
		byte[] excelContent = reportService.exportUnCheckInListToExcelAsByteArray(activateId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=UnCheckInList.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelContent.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelContent);
		
	}
	
	@GetMapping("/checkin/excel")
	public ResponseEntity<byte[]> getAllCheckInList(@RequestParam("activateId") Long activateId) {
		
		logger.info("Received request to get check in count by activation id " + activateId);
		
		byte[] excelContent = reportService.exportCheckInListToExcelAsByteArray(activateId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CheckInList.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelContent.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelContent);
		
	}
	
	@GetMapping("/notireadlog")
	public ResponseEntity<ResponseList<NotiReadLogDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam("emergencyId") Long emergencyId,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<NotiReadLogDto> response = new ResponseList<>();
		Message message = new Message();
		List<NotiReadLogDto> auditLogDtoList = new ArrayList<>();
		logger.info("Received request to search auditlog with data: {}", page,size,params,sortBy,direction);
		Page<NotiReadLogDto> auditLogPage = reportService.searchByParams(page, size, params, sortBy, direction,emergencyId);
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
	
	@GetMapping("/notireadlog/excel")
	public ResponseEntity<byte[]> getAllNotiReadLog(@RequestParam("activateId") Long activateId) {
		
		logger.info("Received request to get noti read log by activation id " + activateId);
		
		byte[] excelContent = reportService.exportCheckInListToExcelAsByteArray(activateId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=NotiReadLogList.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelContent.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelContent);
		
	}
}
