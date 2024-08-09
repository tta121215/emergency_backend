package com.emergency.rollcall.dto;

public class AssemblyPointCheckInDto {

	private Long assemlbySyskey;
	private String assemblyPointName;
	private Long checkInCount;
	private String totalTimeTaken;

	public AssemblyPointCheckInDto(String assemblyPointName, Long checkInCount, Long assemblySyskey, String totalTimeTaken) {
		this.assemblyPointName = assemblyPointName;
		this.checkInCount = checkInCount;
		this.assemlbySyskey = assemblySyskey;
		this.totalTimeTaken = totalTimeTaken;
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

	public Long getAssemlbySyskey() {
		return assemlbySyskey;
	}

	public void setAssemlbySyskey(Long assemlbySyskey) {
		this.assemlbySyskey = assemlbySyskey;
	}

	public String getTotalTimeTaken() {
		return totalTimeTaken;
	}

	public void setTotalTimeTaken(String totalTimeTaken) {
		this.totalTimeTaken = totalTimeTaken;
	}
}
