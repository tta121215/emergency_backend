package com.emergency.rollcall.dto;

import java.util.List;

public class EmergencyRollCallDto {
	private long syskey;
	private String name;
	private int status;
	private String remark;
	private String activateDate;
	private String activateTime;
	private String startTime;
	private String startDate;
	private String endTime;
	private String endDate;
	private int activateStatus;
	private String totalTime;
	private Long totalCheckIn;
	private String totalNotCheckIn;
	private double averageTime;
	private String conditionName;
	private String emergencyName;
	private String emegencyLocation;

	public long getSyskey() {
		return syskey;
	}

	public void setSyskey(long syskey) {
		this.syskey = syskey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getActivateDate() {
		return activateDate;
	}

	public void setActivateDate(String activateDate) {
		this.activateDate = activateDate;
	}

	public String getActivateTime() {
		return activateTime;
	}

	public void setActivateTime(String activateTime) {
		this.activateTime = activateTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getActivateStatus() {
		return activateStatus;
	}

	public void setActivateStatus(int activateStatus) {
		this.activateStatus = activateStatus;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public Long getTotalCheckIn() {
		return totalCheckIn;
	}

	public void setTotalCheckIn(Long totalCheckIn) {
		this.totalCheckIn = totalCheckIn;
	}

	public String getTotalNotCheckIn() {
		return totalNotCheckIn;
	}

	public void setTotalNotCheckIn(String totalNotCheckIn) {
		this.totalNotCheckIn = totalNotCheckIn;
	}

	public double getAverageTime() {
		return averageTime;
	}

	public void setAverageTime(double averageTime) {
		this.averageTime = averageTime;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getEmergencyName() {
		return emergencyName;
	}

	public void setEmergencyName(String emergencyName) {
		this.emergencyName = emergencyName;
	}

	public String getEmegencyLocation() {
		return emegencyLocation;
	}

	public void setEmegencyLocation(String emegencyLocation) {
		this.emegencyLocation = emegencyLocation;
	}

}
