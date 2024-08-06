package com.emergency.rollcall.dto;

public class AssemblyCheckInDto {

	private long syskey;
	private String createddate;
	private String modifieddate;

	private long staffId;
	private String latitude;
	private String longtiude;
	private long assemblyPoint;
	private String deviceType;
	private long emergencySyskey;
	private String currentdate;
	private String currenttime;
	private int status;


	public long getSyskey() {
		return syskey;
	}

	public void setSyskey(long syskey) {
		this.syskey = syskey;
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

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtiude() {
		return longtiude;
	}

	public void setLongtiude(String longtiude) {
		this.longtiude = longtiude;
	}

	public long getAssemblyPoint() {
		return assemblyPoint;
	}

	public void setAssemblyPoint(long assemblyPoint) {
		this.assemblyPoint = assemblyPoint;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public long getEmergencySyskey() {
		return emergencySyskey;
	}

	public void setEmergencySyskey(long emergencySyskey) {
		this.emergencySyskey = emergencySyskey;
	}

	public String getCurrentdate() {
		return currentdate;
	}

	public void setCurrentdate(String currentdate) {
		this.currentdate = currentdate;
	}

	public String getCurrenttime() {
		return currenttime;
	}

	public void setCurrenttime(String currenttime) {
		this.currenttime = currenttime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "AssemblyCheckInDto [syskey=" + syskey + ", createddate=" + createddate + ", modifieddate="
				+ modifieddate + ", staffId=" + staffId + ", latitude=" + latitude + ", longtiude=" + longtiude
				+ ", assemblyPoint=" + assemblyPoint + ", deviceType=" + deviceType + ", emergencySyskey="
				+ emergencySyskey + ", currentdate=" + currentdate + ", currenttime=" + currenttime + ", status=" + status + "]";
	}
}
