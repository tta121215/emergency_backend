package com.emergency.rollcall.dto;

import java.util.List;

public class EmergencyActivateDto {
	private long syskey;
	private String name;
	private int status;
	private String remark;
	private String activateDate;
	private String activateTime;
	private String createddate;
	private String modifieddate;
	private long emergency_syskey;
	private long condition_syskey;
	private List<AssemblyDto> assemblyDtoList;
	private List<RouteDto> routeDtoList;
	private List<LocEmergencyDto> locEmergencyDtoList;
	private List<Long>locemrgency_syskey;
	private String emergency_location;
	private EmergencyDto emergencyDto;
	private ConditionDto conditionDto;
	private String startTime;
	private String startDate;
	private String endTime;
	private String endDate;
	private int activateStatus;
	private int isDelete;

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

	public long getEmergency_syskey() {
		return emergency_syskey;
	}

	public void setEmergency_syskey(long emergency_syskey) {
		this.emergency_syskey = emergency_syskey;
	}

	public long getCondition_syskey() {
		return condition_syskey;
	}

	public void setCondition_syskey(long condition_syskey) {
		this.condition_syskey = condition_syskey;
	}

	public List<LocEmergencyDto> getLocEmergencyDtoList() {
		return locEmergencyDtoList;
	}

	public void setLocEmergencyDtoList(List<LocEmergencyDto> locEmergencyDtoList) {
		this.locEmergencyDtoList = locEmergencyDtoList;
	}

	public List<AssemblyDto> getAssemblyDtoList() {
		return assemblyDtoList;
	}

	public void setAssemblyDtoList(List<AssemblyDto> assemblyDtoList) {
		this.assemblyDtoList = assemblyDtoList;
	}

	public EmergencyDto getEmergencyDto() {
		return emergencyDto;
	}

	public void setEmergencyDto(EmergencyDto emergencyDto) {
		this.emergencyDto = emergencyDto;
	}

	public ConditionDto getConditionDto() {
		return conditionDto;
	}

	public void setConditionDto(ConditionDto conditionDto) {
		this.conditionDto = conditionDto;
	}

	public List<RouteDto> getRouteDtoList() {
		return routeDtoList;
	}

	public void setRouteDtoList(List<RouteDto> routeDtoList) {
		this.routeDtoList = routeDtoList;
	}
	
	public List<Long> getLocemrgency_syskey() {
		return locemrgency_syskey;
	}

	public void setLocemrgency_syskey(List<Long> locemrgency_syskey) {
		this.locemrgency_syskey = locemrgency_syskey;
	}
	
	public String getEmergency_location() {
		return emergency_location;
	}

	public void setEmergency_location(String emergency_location) {
		this.emergency_location = emergency_location;
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

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "EmergencyActivateDto{" + "syskey=" + syskey + ", name='" + name + '\'' + ", remark='" + remark + '\''
				+ ", status='" + status + '\'' + ", createddate=" + createddate + ", modifieddate=" + modifieddate
				+ ", activateDate='" + activateDate + '\'' + ", activateTime='" + activateTime + '\''
				+ ", emergency_syskey='" + emergency_syskey + '\'' + ", condition_syskey='" + condition_syskey + '\''
				+ ", locEmergencyDtoList='" + locEmergencyDtoList + '\'' + ", assemblyDtoList='" + assemblyDtoList
				+ '\'' + ", routeDtoList='" + routeDtoList + '\'' + ", emergencyDto='" + emergencyDto + '\''
				+ ", conditionDto='" + conditionDto + '\'' + '}';
	}

}
