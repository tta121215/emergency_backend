package com.emergency.rollcall.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;
import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.NotiTemplateDto;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.service.NotiTemplateService;


@RestController
@CrossOrigin
@RequestMapping("notitemplate")
public class NotiTemplateController {

	private static final Logger logger = LoggerFactory.getLogger(NotiTemplateController.class);

	@Autowired
	private NotiTemplateService notiTemplateService;

	@PostMapping("")
	public ResponseEntity<Response<ResponseDto>> saveNotification(@RequestBody NotiTemplateDto notiTemplateDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to save notification with data: {}", notiTemplateDto);
		if (notiTemplateDto != null) {
			responseDto = notiTemplateService.saveNotiTemplate(notiTemplateDto);
			if (responseDto.getMessage().equals("Successfully Saved")) {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to save notification with data: {}", message.getMessage());
			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Error to save notification with data: {}", message.getMessage());
			}

		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Error save notification");
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("{id}")
	public ResponseEntity<Response<NotiTemplateDto>> getById(@PathVariable long id) {
		NotiTemplateDto notiTemplateDto = new NotiTemplateDto();
		Response<NotiTemplateDto> response = new Response<>();
		Message message = new Message();
		logger.info("Received request to retrieve notification with data: {}", id);

		if (id != 0) {
			notiTemplateDto = notiTemplateService.getById(id);
			if (notiTemplateDto.getSyskey() != 0) {
				message.setState(true);
				message.setCode("200");
				message.setMessage("Data is successfully");
				logger.info("Successfully to save notification with data: {}", notiTemplateDto);

			} else {
				message.setState(false);
				message.setCode("401");
				message.setMessage("No Data found");
				logger.info("Data does not found to retrieve notification with data: {}", notiTemplateDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No Data found");
			logger.info("Data does not found to retrieve notification with data: {}", notiTemplateDto);
		}
		response.setMessage(message);
		response.setData(notiTemplateDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<Response<ResponseDto>> updateNotification(@RequestBody NotiTemplateDto notiTemplateDto) {
		Response<ResponseDto> response = new Response<>();
		Message message = new Message();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to update notification with data: {}", notiTemplateDto);
		if (notiTemplateDto != null) {
			responseDto = notiTemplateService.updateNotiTemplate(notiTemplateDto);
			if (responseDto.getMessage().equals("Data does not found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update notification with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to update notification with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("404");
			message.setMessage("Data does not dound");
			logger.info("Data does not found to update notification with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Response<ResponseDto>> deleteNotification(@PathVariable("id") long id) {
		Message message = new Message();
		Response<ResponseDto> response = new Response<>();
		ResponseDto responseDto = new ResponseDto();
		logger.info("Received request to delete notification with data: {}", id);

		if (id != 0) {
			responseDto = notiTemplateService.deleteNotiTemplate(id);
			if (responseDto.getMessage().equals("No data found")) {
				message.setState(false);
				message.setCode("401");
				message.setMessage(responseDto.getMessage());
				logger.info("Data does not found to update notification with data: {}", responseDto);
			} else {
				message.setState(true);
				message.setCode("200");
				message.setMessage(responseDto.getMessage());
				logger.info("Successfully to update notification with data: {}", responseDto);
			}
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("No data found");
			logger.info("Received request to update notification with data: {}", responseDto);
		}
		response.setMessage(message);
		response.setData(responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
