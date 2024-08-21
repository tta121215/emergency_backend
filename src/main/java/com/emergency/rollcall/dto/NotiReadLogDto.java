package com.emergency.rollcall.dto;

public class NotiReadLogDto {
	private Long syskey;
	private String createdDate;
	private String createdTime;
	private String staffId;
	private long emergencyId;
	private String readNotiDate;
	private String readNotiTiime;
	private String userName;
	private String emergencyName;
	
	public Long getSyskey() {
		return syskey;
	}
	public void setSyskey(Long syskey) {
		this.syskey = syskey;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public long getEmergencyId() {
		return emergencyId;
	}
	public void setEmergencyId(long emergencyId) {
		this.emergencyId = emergencyId;
	}
	public String getReadNotiDate() {
		return readNotiDate;
	}
	public void setReadNotiDate(String readNotiDate) {
		this.readNotiDate = readNotiDate;
	}
	public String getReadNotiTiime() {
		return readNotiTiime;
	}
	public void setReadNotiTiime(String readNotiTiime) {
		this.readNotiTiime = readNotiTiime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmergencyName() {
		return emergencyName;
	}
	public void setEmergencyName(String emergencyName) {
		this.emergencyName = emergencyName;
	}
	
}
