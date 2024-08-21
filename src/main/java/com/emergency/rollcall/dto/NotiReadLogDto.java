package com.emergency.rollcall.dto;

public class NotiReadLogDto {
	private Long syskey;
	private String createdDate;
	private String createdTime;
	private String staffId;
	private long emergencyId;
	
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
	
}
