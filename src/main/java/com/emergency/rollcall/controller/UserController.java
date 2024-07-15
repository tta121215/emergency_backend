package com.emergency.rollcall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.emergency.rollcall.dto.Message;
import com.emergency.rollcall.dto.Response;
import com.emergency.rollcall.dto.UserDto;
import com.emergency.rollcall.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("appuser")
public class UserController {

	@Autowired
	private UserService userservice;

	@PostMapping("/login")
	public ResponseEntity<Response<UserDto>> login(@RequestBody UserDto userDTO) {
		Response<UserDto> response = new Response<>();
		Message message = new Message();
		UserDto userDto = userservice.login(userDTO.getUsername(), userDTO.getPassword());
		if (userDto != null && "Login Sucess".equals(userDto.getToken())) {
			message.setState(true);
			message.setCode("200");
			message.setMessage("Login success");
		} else {
			message.setState(false);
			message.setCode("401");
			message.setMessage("Login failed");
		}

		response.setMessage(message);
		response.setData(userDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
