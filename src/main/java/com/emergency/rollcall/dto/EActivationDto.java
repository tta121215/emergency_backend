package com.emergency.rollcall.dto;

import java.util.List;

public class EActivationDto {

	private List<ModeNotiDto> modeNotiDtoList;
	private EActivateSubjectDto esubjectDto;
	private EActivateSubjectDto econtentDto;

	public List<ModeNotiDto> getModeNotiDtoList() {
		return modeNotiDtoList;
	}

	public void setModeNotiDtoList(List<ModeNotiDto> modeNotiDtoList) {
		this.modeNotiDtoList = modeNotiDtoList;
	}

	public EActivateSubjectDto getEsubjectDto() {
		return esubjectDto;
	}

	public void setEsubjectDto(EActivateSubjectDto esubjectDto) {
		this.esubjectDto = esubjectDto;
	}

	public EActivateSubjectDto getEcontentDto() {
		return econtentDto;
	}

	public void setEcontentDto(EActivateSubjectDto econtentDto) {
		this.econtentDto = econtentDto;
	}

}
