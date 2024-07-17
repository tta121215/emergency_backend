package com.emergency.rollcall.service;

import org.springframework.data.domain.Page;
import com.emergency.rollcall.dto.ModeNotiDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface ModeNotiService {

	ResponseDto saveModeNoti(ModeNotiDto modeNotiDto);

	ModeNotiDto getById(long id);

	ResponseDto updateModeNoti(ModeNotiDto modeNotiDto);

	ResponseDto deleteModeNoti(long id);

	Page<ModeNotiDto> searchByParams(int page, int size, String params);

}
