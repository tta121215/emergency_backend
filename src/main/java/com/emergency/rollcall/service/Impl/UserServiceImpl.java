package com.emergency.rollcall.service.Impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emergency.rollcall.dao.UserDao;
import com.emergency.rollcall.dto.UserDto;
import com.emergency.rollcall.entity.User;
import com.emergency.rollcall.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userdao;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDto login(String username, String password) {
		UserDto res = new UserDto();
		User user = userdao.findByUsername(username);
		if (user != null) {
			if (user.getStatus() == 1) {
				if (passwordEncoder.matches(password, user.getPassword())) {
					res = modelMapper.map(user, UserDto.class);
					res.setPassword("");
					res.setToken("Login Sucess");
				} else {
					res.setToken("Invalid username and password");
				}
			} else {
				res.setToken("User is inactive");
			}
		} else {
			res.setToken("No user found");
		}		
		return res; 
	}

}
