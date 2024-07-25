package com.emergency.rollcall.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Assembly_check_in")
public class AssemblyCheckIn {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_seq_gen")
	@SequenceGenerator(name = "example_seq_gen", sequenceName = "example_seq", allocationSize = 1)
	private long syskey;

	private String createddate;
	private String modifieddate;
	private long staffId;
	private String latitude;
	private String longtiude;
	private long assemblyPoint;
	private String deviceType;
	private long emergencySyskey;
	private String currentdate;
	private String currenttime;

	public long getSyskey() {
		return syskey;
	}

	public void setSyskey(long syskey) {
		this.syskey = syskey;
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

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtiude() {
		return longtiude;
	}

	public void setLongtiude(String longtiude) {
		this.longtiude = longtiude;
	}

	public long getAssemblyPoint() {
		return assemblyPoint;
	}

	public void setAssemblyPoint(long assemblyPoint) {
		this.assemblyPoint = assemblyPoint;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public long getEmergencySyskey() {
		return emergencySyskey;
	}

	public void setEmergencySyskey(long emergencySyskey) {
		this.emergencySyskey = emergencySyskey;
	}

	public String getCurrentdate() {
		return currentdate;
	}

	public void setCurrentdate(String currentdate) {
		this.currentdate = currentdate;
	}

	public String getCurrenttime() {
		return currenttime;
	}

	public void setCurrenttime(String currenttime) {
		this.currenttime = currenttime;
	}

}
