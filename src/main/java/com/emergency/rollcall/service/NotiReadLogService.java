package com.emergency.rollcall.service;

import org.springframework.data.domain.Page;
import com.emergency.rollcall.dto.NotiReadLogDto;
import com.emergency.rollcall.dto.ResponseDto;


public interface NotiReadLogService {

	ResponseDto saveNotiReadLog(NotiReadLogDto notiReadLogDto);   	
	
	Page<NotiReadLogDto> searchByParams(int page, int size, String params, String sortBy, String direction);
	

}
