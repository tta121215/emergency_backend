package com.emergency.rollcall.dto;

public class AssemblyPointCheckInDto {

	private String assemblyPointName;
	private Long checkInCount;

	public AssemblyPointCheckInDto(String assemblyPointName, Long checkInCount) {
		this.assemblyPointName = assemblyPointName;
		this.checkInCount = checkInCount;
	}

	public String getAssemblyPointName() {
		return assemblyPointName;
	}

	public void setAssemblyPointName(String assemblyPointName) {
		this.assemblyPointName = assemblyPointName;
	}

	public Long getCheckInCount() {
		return checkInCount;
	}

	public void setCheckInCount(Long checkInCount) {
		this.checkInCount = checkInCount;
	}
}
