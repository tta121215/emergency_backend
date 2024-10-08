package com.emergency.rollcall.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ERC_Route")
public class Route {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_seq_gen")
	@SequenceGenerator(name = "example_seq_gen", sequenceName = "example_seq", allocationSize = 1)
	private long syskey;
	private String name;
	private String description;
	private int status;
	private String createddate;
	private String modifieddate;
	private int isDelete;
	
	@Lob
    @Column(name = "data", columnDefinition = "BLOB")
    private byte[] data;
	
	@Column(name = "attach_name")
	private String attachName;

	@ManyToMany(mappedBy = "routeList")
	private List<LocEmergency> locEmergencyList = new ArrayList<>();

	@ManyToMany(mappedBy = "routeList")
	private List<EmergencyActivate> emergencyActivatesList = new ArrayList<>();

	public long getSyskey() {
		return syskey;
	}

	public List<EmergencyActivate> getEmergencyActivatesList() {
		return emergencyActivatesList;
	}

	public void setEmergencyActivatesList(List<EmergencyActivate> emergencyActivatesList) {
		this.emergencyActivatesList = emergencyActivatesList;
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

	public List<LocEmergency> getLocEmergencyList() {
		return locEmergencyList;
	}

	public void setLocEmergencyList(List<LocEmergency> locEmergencyList) {
		this.locEmergencyList = locEmergencyList;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

}
