package com.emergency.rollcall.dto;

import java.util.List;

public class NotificationDto {
	private long syskey;
	private String notimode;
	private String notisubject;
	private String notimessage;
	private int status;
	private String createddate;
	private String modifieddate;
	// private List<ModeNotiDto> modeNotiDto;
	private long emergencySyskey;
	private EmergencyDto emergencyDto;

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

	public long getEmergencySyskey() {
		return emergencySyskey;
	}

	public void setEmergencySyskey(long emergencySyskey) {
		this.emergencySyskey = emergencySyskey;
	}

	public EmergencyDto getEmergencyDto() {
		return emergencyDto;
	}

	public void setEmergencyDto(EmergencyDto emergencyDto) {
		this.emergencyDto = emergencyDto;
	}

	@Override
	public String toString() {
		return "NotificationDto{" + "syskey=" + syskey + ", notimode='" + notimode + '\'' + ", notisubject='"
				+ notisubject + '\'' + ", notimessage='" + notimessage + '\'' + ", emergencySyskey='" + emergencySyskey
				+ '\'' + ", emergencyDto='" + emergencyDto + '\'' + ", status='" + status + '\'' + ", createddate="
				+ createddate + ", modifieddate=" + modifieddate + '}';
	}
}
