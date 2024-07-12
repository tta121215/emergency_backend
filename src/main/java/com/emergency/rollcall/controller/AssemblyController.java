package com.emergency.rollcall.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

	@Autowired
	private AssemblyService assemblyService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveAssembly(@RequestBody AssemblyDto assemblyDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (assemblyDto != null) {
			responseDto = assemblyService.saveAssembly(assemblyDto);
			message.setCode("200");
			message.setMessage("Save successfully");
		} else {
			message.setCode("401");
			message.setMessage("Error Save assembly");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("")
	public ResponseEntity<Response<AssemblyDto>> getById(@RequestParam("id") Long id) {
		AssemblyDto assemblyDto = new AssemblyDto();
		Response<AssemblyDto> response = new Response<>();
		Message message = new Message();

		if (id != null) {
			assemblyDto = assemblyService.getById(id);
			if (assemblyDto.getSyskey() != 0) {
				message.setCode("200");
				message.setMessage("Data is successfully");

			} else {
				message.setCode("401");
				message.setMessage("No Data found");
			}
		} else {
			message.setCode("401");
			message.setMessage("No Data found");
		}
		response.setMessage(message);
		response.setData(assemblyDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/update")
	public ResponseEntity<Response<ResponseDto>> updateAssebmly(@RequestBody AssemblyDto data) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (data != null) {
			responseDto = assemblyService.updateAssembly(data);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
			} else {
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
			}
		} else {
			message.setCode("404");
			message.setMessage("Data does not dound");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/delete")
	public ResponseEntity<Response<ResponseDto>> deleteAssebmly(@RequestBody AssemblyDto data) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		if (data != null) {
			responseDto = assemblyService.deleteAssembly(data);
			if (responseDto.getMessage().equals("No data found")) {
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
			} else {
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
			}
		} else {
			message.setCode("401");
			message.setMessage("No data found");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/assembly-list")
	public ResponseEntity<ResponseList<AssemblyDto>> assemblyList() { 		
		ResponseList<AssemblyDto> response = new ResponseList<>();
		Message message = new Message();
		List<AssemblyDto> assemblyDtoList = new ArrayList<>();
		assemblyDtoList = assemblyService.getAllList();

		if (!assemblyDtoList.isEmpty()) {
			message.setCode("200");
			message.setMessage("Data is successfully");

		} else {
			message.setCode("401");
			message.setMessage("No Data found");
		}

		response.setMessage(message);
		response.setData(assemblyDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
