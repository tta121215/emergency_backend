package com.emergency.rollcall.entity;


import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.emergency.rollcall.service.BaseEntity;

@Entity
@Table(name = "Emergency")
public class Emergency implements BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_seq_gen")
	@SequenceGenerator(name = "example_seq_gen", sequenceName = "example_seq", allocationSize = 1)
	private long syskey;
	private String name;
	private String code;
	private int status;
	private String createddate;
	private String modifieddate;
//
//	@ManyToMany(mappedBy = "emergencyList")
//	private List<Notification> notificationList = new ArrayList<>();

//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "emergency_activate_syskey")
//    private EmergencyActivate emergencyActivate;

	public Long getSyskey() {
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	
	
//	public EmergencyActivate getEmergencyActivate() {
//		return emergencyActivate;
//	}
//
//	public void setEmergencyActivate(EmergencyActivate emergencyActivate) {
//		this.emergencyActivate = emergencyActivate;
//	}

}
