package com.emergency.rollcall.dto;

import java.util.List;

public class DashboardResponseDto {

	private List<AssemblyPointCheckInDto> checkInCounts;
	//private List<AssemblyPointNotCheckInDto> notCheckInCounts;
	private Long totalCheckInCount;
	private Long totalNotCheckInCount;
	private String totalTime;
	private Double averageTime;

	
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

	public Double getAverageTime() {
		return averageTime;
	}

	public void setAverageTime(Double averageTime) {
		this.averageTime = averageTime;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
}
