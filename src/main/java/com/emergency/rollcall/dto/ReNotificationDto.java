package com.emergency.rollcall.dto;

public class ReNotificationDto {
	private long syskey;
	private float time;
	private int frequency;
	private int status;
	private String createddate;
	private String modifieddate;
	private int isDelete;

	public long getSyskey() {
		return syskey;
	}

	public void setSyskey(long syskey) {
		this.syskey = syskey;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
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

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "ReNotificationDto{" + "syskey=" + syskey + ", time='" + time + '\'' + ", frequency='" + frequency + '\''
				+ ", status='" + status + '\'' + ", createddate=" + createddate + ", modifieddate=" + modifieddate
				+ ", isDelete=" + isDelete + '}';
	}

}
