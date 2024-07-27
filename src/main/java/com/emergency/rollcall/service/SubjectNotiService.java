package com.emergency.rollcall.service;

import java.util.List;

import org.springframework.data.domain.Page;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.SubjectNotiDto;

public interface SubjectNotiService {

	ResponseDto saveSubjectNoti(SubjectNotiDto subjectNotiDto);

	SubjectNotiDto getById(long id);

	ResponseDto updateSubjectNoti(SubjectNotiDto subjectNotiDto);

	ResponseDto deleteSubjectNoti(long id);

	Page<SubjectNotiDto> searchByParams(int page, int size, String params, String sortBy, String direction);

	List<SubjectNotiDto> getAllList();

}
