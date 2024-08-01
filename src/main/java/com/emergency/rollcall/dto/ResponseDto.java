package com.emergency.rollcall.dto;

public class ResponseDto {
	private int status_code;
	private String message;
	private long emergencySyskey;
	public int getStatus_code() {
		return status_code;
	}
	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getEmergencySyskey() {
		return emergencySyskey;
	}
	public void setEmergencySyskey(long emergencySyskey) {
		this.emergencySyskey = emergencySyskey;
	}

}
