package com.emergency.rollcall.service;

import com.emergency.rollcall.dto.UserDto;

public interface UserService {

	UserDto login(String username, String password);
}
