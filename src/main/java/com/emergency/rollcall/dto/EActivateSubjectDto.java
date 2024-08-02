package com.emergency.rollcall.dto;

import java.util.List;

public class EActivateSubjectDto {
	
	private EmergencyDto emergencyDto;
	private List<RouteDto> routeDtoList;
	private List<AssemblyDto> assemblyDtoList;
	private ConditionDto conditionDto;
	private List<LocEmergencyDto> locEmergencyDtoList;
	private String date;
	private String time;
	private String othersDes;
	public EmergencyDto getEmergencyDto() {
		return emergencyDto;
	}
	public void setEmergencyDto(EmergencyDto emergencyDto) {
		this.emergencyDto = emergencyDto;
	}
	public List<RouteDto> getRouteDtoList() {
		return routeDtoList;
	}
	public void setRouteDtoList(List<RouteDto> routeDtoList) {
		this.routeDtoList = routeDtoList;
	}
	public List<AssemblyDto> getAssemblyDtoList() {
		return assemblyDtoList;
	}
	public void setAssemblyDtoList(List<AssemblyDto> assemblyDtoList) {
		this.assemblyDtoList = assemblyDtoList;
	}
	public ConditionDto getConditionDto() {
		return conditionDto;
	}
	public void setConditionDto(ConditionDto conditionDto) {
		this.conditionDto = conditionDto;
	}
	public List<LocEmergencyDto> getLocEmergencyDtoList() {
		return locEmergencyDtoList;
	}
	public void setLocEmergencyDtoList(List<LocEmergencyDto> locEmergencyDtoList) {
		this.locEmergencyDtoList = locEmergencyDtoList;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getOthersDes() {
		return othersDes;
	}
	public void setOthersDes(String othersDes) {
		this.othersDes = othersDes;
	}
	
	
}
