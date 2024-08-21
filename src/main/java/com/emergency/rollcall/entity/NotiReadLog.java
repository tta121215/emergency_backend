package com.emergency.rollcall.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ERC_NotiRead_Log")
public class NotiReadLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_seq_gen")
	@SequenceGenerator(name = "audit_seq_gen", sequenceName = "audit_seq", allocationSize = 1)
	private long syskey;
	private String createdDate;
	private String createdTime;
	private String readNotiDate;
	private String readNotiTime;
	private String staffId;
	private long emergencyId;
	
	public long getSyskey() {
		return syskey;
	}
	public void setSyskey(long syskey) {
		this.syskey = syskey;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public long getEmergencyId() {
		return emergencyId;
	}
	public void setEmergencyId(long emergencyId) {
		this.emergencyId = emergencyId;
	}
	public String getReadNotiDate() {
		return readNotiDate;
	}
	public void setReadNotiDate(String readNotiDate) {
		this.readNotiDate = readNotiDate;
	}
	public String getReadNotiTime() {
		return readNotiTime;
	}
	public void setReadNotiTime(String readNotiTime) {
		this.readNotiTime = readNotiTime;
	}
	
}
