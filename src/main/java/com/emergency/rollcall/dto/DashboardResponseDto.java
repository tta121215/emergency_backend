package com.emergency.rollcall.dto;

import java.util.List;

public class DashboardResponseDto {

	private List<AssemblyPointCheckInDto> checkInCounts;
	private Long totalCheckInCount;

	public DashboardResponseDto(List<AssemblyPointCheckInDto> checkInCounts, Long totalCheckInCount) {
		this.checkInCounts = checkInCounts;
		this.totalCheckInCount = totalCheckInCount;
	}

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
}
