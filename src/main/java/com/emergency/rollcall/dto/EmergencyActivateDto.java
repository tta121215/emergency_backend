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
	private long locemrgency_syskey;
	private long notification_syskey;
	private List<AssemblyDto> assemblyDtoList;
	private List<RouteDto> routeDtoList;
	private EmergencyDto emergencyDto;
	private ConditionDto conditionDto;
	private LocEmergencyDto locEmergencyDto;
	private NotificationDto notificationDto;

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

	public long getLocemrgency_syskey() {
		return locemrgency_syskey;
	}

	public void setLocemrgency_syskey(long locemrgency_syskey) {
		this.locemrgency_syskey = locemrgency_syskey;
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

	public LocEmergencyDto getLocEmergencyDto() {
		return locEmergencyDto;
	}

	public void setLocEmergencyDto(LocEmergencyDto locEmergencyDto) {
		this.locEmergencyDto = locEmergencyDto;
	}

	public List<RouteDto> getRouteDtoList() {
		return routeDtoList;
	}

	public void setRouteDtoList(List<RouteDto> routeDtoList) {
		this.routeDtoList = routeDtoList;
	}

	public long getNotification_syskey() {
		return notification_syskey;
	}

	public void setNotification_syskey(long notification_syskey) {
		this.notification_syskey = notification_syskey;
	}

	public NotificationDto getNotificationDto() {
		return notificationDto;
	}

	public void setNotificationDto(NotificationDto notificationDto) {
		this.notificationDto = notificationDto;
	}

	@Override
	public String toString() {
		return "EmergencyActivateDto{" + "syskey=" + syskey + ", name='" + name + '\'' + ", remark='" + remark + '\''
				+ ", status='" + status + '\'' + ", createddate=" + createddate + ", modifieddate=" + modifieddate
				+ ", activateDate='" + activateDate + '\'' + ", activateTime='" + activateTime + '\''
				+ ", emergency_syskey='" + emergency_syskey + '\'' + ", condition_syskey='" + condition_syskey + '\''
				+ ", locemrgency_syskey='" + locemrgency_syskey + '\'' + ", assemblyDtoList='" + assemblyDtoList + '\''
				+ ", routeDtoList='" + routeDtoList + '\'' + ", emergencyDto='" + emergencyDto + '\''
				+ ", conditionDto='" + conditionDto + '\'' + ", locEmergencyDto='" + locEmergencyDto + '\'' + '}';
	}

}
