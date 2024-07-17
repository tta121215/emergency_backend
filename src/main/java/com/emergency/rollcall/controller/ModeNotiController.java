package com.emergency.rollcall.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

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

import com.emergency.rollcall.dto.ModeNotiDto;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ResponseList;
import com.emergency.rollcall.service.ModeNotiService;

@RestController
@CrossOrigin
@RequestMapping("modenoti")
public class ModeNotiController {

	@Autowired
	private ModeNotiService modeNotiService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveModeNoti(@RequestBody ModeNotiDto ModeNotiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (ModeNotiDto != null) {
			responseDto = modeNotiService.saveModeNoti(ModeNotiDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error Save");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<ModeNotiDto>> getById(@PathVariable Long id) {
		ModeNotiDto ModeNotiDto = new ModeNotiDto();
		Response<ModeNotiDto> response = new Response<>();
		Message message = new Message();

		if (id != null) {
			ModeNotiDto = modeNotiService.getById(id);
			if (ModeNotiDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
		}
		response.setMessage(message);
		response.setData(ModeNotiDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateLocEmergency(@RequestBody ModeNotiDto ModeNotiDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		if (ModeNotiDto != null) {
			responseDto = modeNotiService.updateModeNoti(ModeNotiDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
			} else {
				message.setState(true);
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

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteLocEmergency(@PathVariable("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		if (id != 0) {
			responseDto = modeNotiService.deleteModeNoti(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<ResponseList<ModeNotiDto>> searchByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("params") String params) {

		ResponseList<ModeNotiDto> response = new ResponseList<>();
		Message message = new Message();
		List<ModeNotiDto> ModeNotiDtoList = new ArrayList<>();
		Page<ModeNotiDto> modeNotiPage = modeNotiService.searchByParams(page, size, params);
		ModeNotiDtoList = modeNotiPage.getContent();
		if (!ModeNotiDtoList.isEmpty()) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Data is successfully");

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
		}

		response.setMessage(message);
		response.setData(ModeNotiDtoList);
		response.setTotalItems(modeNotiPage.getTotalElements());
		response.setTotalPages(modeNotiPage.getTotalPages());
		response.setCurrentPage(page);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
