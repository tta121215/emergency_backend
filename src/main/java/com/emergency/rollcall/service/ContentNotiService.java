package com.emergency.rollcall.service;

import java.util.List;
import org.springframework.data.domain.Page;

import com.emergency.rollcall.dto.ContentNotiDto;
import com.emergency.rollcall.dto.ResponseDto;

public interface ContentNotiService {

	ResponseDto saveContentNoti(ContentNotiDto contentNotiDto);

	ContentNotiDto getById(long id);

	ResponseDto updateContentNoti(ContentNotiDto contentNotiDto);

	ResponseDto deleteContentNoti(long id);

	Page<ContentNotiDto> searchByParams(int page, int size, String params, String sortBy, String direction);

	List<ContentNotiDto> getAllList();

}
