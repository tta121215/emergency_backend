package com.emergency.rollcall.dto;

import java.util.List;

public class DashboardResponseDto {

	private List<AssemblyPointCheckInDto> checkInCounts;
	//private List<AssemblyPointNotCheckInDto> notCheckInCounts;
	private Long totalCheckInCount;
	private Long totalNotCheckInCount;
	private String totalTime;
	private String averageTime;
	private Long totalHeadCount;
	private String startTime;

	
	// Getters and Setters
	public List<AssemblyPointCheckInDto> getCheckInCounts() {
		return checkInCounts;
	}

	public void setCheckInCounts(List<AssemblyPointCheckInDto> checkInCounts) {
		this.checkInCounts = checkInCounts;
	}

	public Long getTotalCheckInCount() {
		return totalCheckInCount;
	}

	public void setTotalCheckInCount(Long totalCheckInCount) {
		this.totalCheckInCount = totalCheckInCount;
	}

	public Long getTotalNotCheckInCount() {
		return totalNotCheckInCount;
	}

	public void setTotalNotCheckInCount(Long totalNotCheckInCount) {
		this.totalNotCheckInCount = totalNotCheckInCount;
	}

	public String getAverageTime() {
		return averageTime;
	}

	public void setAverageTime(String averageTime) {
		this.averageTime = averageTime;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public Long getTotalHeadCount() {
		return totalHeadCount;
	}

	public void setTotalHeadCount(Long totalHeadCount) {
		this.totalHeadCount = totalHeadCount;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
}
