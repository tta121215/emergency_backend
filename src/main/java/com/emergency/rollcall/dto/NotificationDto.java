package com.emergency.rollcall.dto;

public class NotificationDto {
	private long syskey;
	private String notimode;
	private String notisubject;
	private String notimessage;
	private String status;
	private String createddate;
	private String modifieddate;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

}
