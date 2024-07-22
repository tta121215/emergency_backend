package com.emergency.rollcall.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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

import com.emergency.rollcall.dto.AssemblyDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.AssemblyService;

@RestController
@CrossOrigin
@RequestMapping("assembly")
public class AssemblyController {

	private static final Logger logger = LoggerFactory.getLogger(AssemblyController.class);
	
	@Autowired
	private AssemblyService assemblyService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveAssembly(@RequestBody AssemblyDto assemblyDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save assembly with data: {}", assemblyDto);
		if (assemblyDto != null) {
			responseDto = assemblyService.saveAssembly(assemblyDto);
			message.setState(true);
			message.setCode("200");
			message.setMessage("Save successfully");
			logger.info("Successfully saved assembly: {}", responseDto);
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save assembly");
			logger.info("Assembly saved error occured");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<AssemblyDto>> getById(@PathVariable Long id) {
		AssemblyDto assemblyDto = new AssemblyDto();
		Response<AssemblyDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to get assembly by id " + id);
		if (id != null) {
			assemblyDto = assemblyService.getById(id);
			if (assemblyDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully retrive data assembly " + assemblyDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Assembly retrieve data error occured " + message.getMessage());
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Assembly retrieve data error occured " + message.getMessage());
		}
		response.setMessage(message);
		response.setData(assemblyDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateAssebmly(@RequestBody AssemblyDto data) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Successfully retrive update data assembly " + data);
		if (data != null) {
			responseDto = assemblyService.updateAssembly(data);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Error update data assembly " + message.getMessage());
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully update data assembly " + message.getMessage());
			}
		} else {
			message.setState(false);
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Error update data assembly " + data);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteAssebmly(@PathVariable("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Successfully retrive delete data assembly " + id);
		if (id != 0) {
			responseDto = assemblyService.deleteAssembly(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Error delete data assembly " + message.getMessage());
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully retrive delete data assembly " + message.getMessage());
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Error delete data assembly " + message.getMessage());
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@GetMapping("")
	public ResponseEntity<ResponseList<AssemblyDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,@RequestParam ("params") String params,
            @RequestParam(defaultValue = "syskey") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) { 		
		ResponseList<AssemblyDto> response = new ResponseList<>();
		Message message = new Message();
		List<AssemblyDto> assemblyDtoList = new ArrayList<>();
		logger.info("Successfully retrive serarch data assembly " + page +" page " + size + " size " + params + " params " + sortBy + " sortBy" + direction + " direction");
		Page<AssemblyDto> assemblyPage = assemblyService.searchByParams(page,size,params,sortBy,direction);
		assemblyDtoList = assemblyPage.getContent();
		if (!assemblyDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");
			logger.info("Successfully retrive data assembly " + assemblyDtoList);

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Successfully retrive update data assembly " + assemblyDtoList);
		}

		response.setMessage(message);
		response.setData(assemblyDtoList);		
		response.setTotalItems(assemblyPage.getTotalElements());
		response.setTotalPages(assemblyPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}


}
