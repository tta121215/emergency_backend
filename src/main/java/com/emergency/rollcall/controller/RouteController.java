package com.emergency.rollcall.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.emergency.rollcall.dto.RouteDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.RouteService;

@RestController
@CrossOrigin
@RequestMapping("route")
public class RouteController {

	private static final Logger logger = LoggerFactory.getLogger(RouteController.class);

	@Autowired
	private RouteService routeService;

//	@PostMapping("")
//	public ResponseEntity<Response<ResponseDto>> saveRoute(@RequestBody RouteDto routeDto) {
//		Response<ResponseDto> response = new Response<>();
//		Message message = new Message();
//		ResponseDto responseDto = new ResponseDto();
//		logger.info("Received request to save route with data: {}", routeDto);
//		if (routeDto != null) {
//			responseDto = routeService.saveRoute(routeDto);
//			if (responseDto.getMessage().equals("Successfully Saved")) {
//				message.setState(true);
//				message.setCode("200");
//				message.setMessage(responseDto.getMessage());
//				logger.info("Successfully to save route with data: {}", routeDto);
//			}
//
//		} else {
//			message.setState(false);
//			message.setCode("401");
//			message.setMessage("Error Save route ");
//			logger.info("Error to save route with data: {}", responseDto);
//		}
//		response.setMessage(message);
//		response.setData(responseDto);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//
//	}

	@GetMapping("{id}")
	public ResponseEntity<Response<RouteDto>> getById(@PathVariable Long id) {
		RouteDto routeDto = new RouteDto();
		Response<RouteDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve route with data: {}", id);
		if (id != null) {
			routeDto = routeService.getById(id);
			if (routeDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully to retrieve route with data: {}", routeDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve route with data: {}", routeDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve route with data: {}", routeDto);
		}
		response.setMessage(message);
		response.setData(routeDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

//	@PutMapping("")
//	public ResponseEntity<Response<ResponseDto>> updateRoute(@RequestBody RouteDto routeDto) {
//		Response<ResponseDto> response = new Response<>();
//		Message message = new Message();
//		ResponseDto responseDto = new ResponseDto();
//		logger.info("Received request to update rouete with data: {}", routeDto);
//		if (routeDto != null) {
//			responseDto = routeService.updateRoute(routeDto);
//			if (responseDto.getMessage().equals("Data does not found")) {
//				message.setState(false);
//				message.setCode("401");
//				message.setMessage(responseDto.getMessage());
//				logger.info("Data does not found to update route with data: {}", responseDto);
//			} else {
//				message.setState(true);
//				message.setCode("200");
//				message.setMessage(responseDto.getMessage());
//				logger.info("Successfully to update update with data: {}", responseDto);
//			}
//		} else {
//			message.setState(false);
//			message.setCode("404");
//			message.setMessage("Data does not dound");
//			logger.info("Data does not found to update route with data: {}", responseDto);
//		}
//		response.setMessage(message);
//		response.setData(responseDto);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
	
	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateRoute(@RequestParam("id") Long id,
			@RequestParam("name") String name,
			@RequestParam("description") String description, @RequestParam("status") Integer status,
			@RequestParam(required = false) MultipartFile attachFiles) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update rouete with data: {}", id);
		if (id != 0) {
			responseDto = routeService.updateRoute(id,name,description,status,attachFiles);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update route with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to update update with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update route with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteRoute(@PathVariable("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to delete route with data: {}", id);
		if (id != 0) {
			responseDto = routeService.deleteRoute(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to delete route with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to delete re-notification with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Data does not found to delete route with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<RouteDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params,
			@RequestParam(defaultValue = "syskey") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		ResponseList<RouteDto> response = new ResponseList<>();
		logger.info("Received request to search route with data: {}", page, size, sortBy, direction);
		Message message = new Message();
		List<RouteDto> routeDtoList = new ArrayList<>();
		Page<RouteDto> routePage = routeService.searchByParams(page, size, params, sortBy, direction);
		routeDtoList = routePage.getContent();
		if (!routeDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to search route with data: {}", routeDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to search route with data: {}", routeDtoList);
		}

		response.setMessage(message);
		response.setData(routeDtoList);
		response.setTotalItems(routePage.getTotalElements());
		response.setTotalPages(routePage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("all-list")
	public ResponseEntity<ResponseList<RouteDto>> getAllList() {

		ResponseList<RouteDto> response = new ResponseList<>();
		Message message = new Message();
		List<RouteDto> routeDtoList = new ArrayList<>();
		routeDtoList = routeService.getAllList();
		logger.info("Received request to retrieve route list : ");

		if (!routeDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully to retrived route list with data: {}", routeDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve route list: {}", routeDtoList);
		}

		response.setMessage(message);
		response.setData(routeDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/locEmergencyId")
	public ResponseEntity<ResponseList<RouteDto>> getByLocationOfEmergency(@RequestParam("id") String id) {
		RouteDto routeDto = new RouteDto();
		ResponseList<RouteDto> response = new ResponseList<>();
		Message message = new Message();
		List<RouteDto> routeDtoList = new ArrayList<>();
		logger.info("Received request to retrieve route by location of emergency with data: {}", id);
		if (id != null) {
			routeDtoList = routeService.getByLocationofEmergency(id);
			if (routeDtoList != null) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully to retrieve route by location of emergency with data: {}", routeDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve route with data: {}", routeDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve route with data: {}", routeDto);
		}
		response.setMessage(message);
		response.setData(routeDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveRouteWithAttach(@RequestParam("name") String name,
			@RequestParam("description") String description, @RequestParam("status") Integer status,
			@RequestParam(required = false) MultipartFile attachFiles) throws IOException {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save route with data: {}", name);

		responseDto = routeService.saveRouteAttach(name, description, status, attachFiles);
		if (responseDto.getMessage().equals("Successfully Saved")) {
			message.setState(true);
			message.setCode("200");
			message.setMessage(responseDto.getMessage());
			logger.info("Successfully to save route with data: {}", name);

			response.setMessage(message);
			response.setData(responseDto);			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}

}
