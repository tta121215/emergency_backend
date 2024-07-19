package com.emergency.rollcall.dto;

import java.util.List;

import com.emergency.rollcall.entity.ModeNoti;

public class NotificationDto {
	private long syskey;
	private String notimode;
	private String notisubject;
	private String notimessage;
	private int status;
	private String createddate;
	private String modifieddate;
	private List<ModeNotiDto> modeNotiDto;

	public long getSyskey() {
		return syskey;
	}

	public void setSyskey(long syskey) {
		this.syskey = syskey;
	}

	public String getNotimode() {
		return notimode;
	}

	public void setNotimode(String notimode) {
		this.notimode = notimode;
	}

	public String getNotisubject() {
		return notisubject;
	}

	public void setNotisubject(String notisubject) {
		this.notisubject = notisubject;
	}

	public String getNotimessage() {
		return notimessage;
	}

	public void setNotimessage(String notimessage) {
		this.notimessage = notimessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreateddate() {
		return createddate;
	}

	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}

	public String getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(String modifieddate) {
		this.modifieddate = modifieddate;
	}

	public List<ModeNotiDto> getModeNotiDto() {
		return modeNotiDto;
	}

	public void setModeNotiDto(List<ModeNotiDto> modeNotiDto) {
		this.modeNotiDto = modeNotiDto;
	}

}
