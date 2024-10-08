package com.emergency.rollcall.dto;

import java.util.List;

public class MainBuildingDto {
	private long syskey;
	private long locationId;
	private String locationName;
	private String description;
	private int status;
	private String createddate;
	private String modifieddate;	
	private int isDelete;
	private List<LocEmergencyDto> locEmergencyList;
	private int isDefault;

	public long getSyskey() {
		return syskey;
	}

	public void setSyskey(long syskey) {
		this.syskey = syskey;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	public List<LocEmergencyDto> getLocEmergencyList() {
		return locEmergencyList;
	}

	public void setLocEmergencyList(List<LocEmergencyDto> locEmergencyList) {
		this.locEmergencyList = locEmergencyList;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public String ddMMyyyFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null)
			// l_Date = aDate.substring(6) + "/" + aDate.substring(4, 6) + "/" +
			// aDate.substring(0, 4);
			l_Date = aDate.substring(0, 4) + "-" + aDate.substring(4, 6) + "-" + aDate.substring(6);

		return l_Date;
	}

	@Override
	public String toString() {
		return "MainBuildingDto [syskey=" + syskey + ", locationId=" + locationId + ", locationName=" + locationName
				+ ", description=" + description + ", status=" + status + ", createddate=" + createddate
				+ ", modifieddate=" + modifieddate + ", isDelete=" + isDelete +"]";
	}

}
