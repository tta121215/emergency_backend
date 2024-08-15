package com.emergency.rollcall.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ERC_Loc_Emergency")
public class LocEmergency {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_seq_gen")
	@SequenceGenerator(name = "example_seq_gen", sequenceName = "example_seq", allocationSize = 1)
	private long syskey;
	private String locationId;
	private String locationName;
	private String description;
	private int status;
	private String createddate;
	private String modifieddate;
	private int isDelete;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
	@JoinTable(name = "ERC_Loc_Route", joinColumns = @JoinColumn(name = "Loc_Emergency_syskey"), inverseJoinColumns = @JoinColumn(name = "route_syskey"))
	private List<Route> routeList = new ArrayList<>();

	@ManyToMany(mappedBy = "locEmergencyList")
	private List<EmergencyActivate> emergencyActivatesList = new ArrayList<>();

	public long getSyskey() {
		return syskey;
	}

	public void setSyskey(long syskey) {
		this.syskey = syskey;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
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

	public List<Route> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<Route> routeList) {
		this.routeList = routeList;
	}
	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

}
