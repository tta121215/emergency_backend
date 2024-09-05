package com.emergency.rollcall.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ERC_Emergency_Activate")
public class EmergencyActivate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_seq_gen")
	@SequenceGenerator(name = "example_seq_gen", sequenceName = "example_seq", allocationSize = 1)
	private long syskey;
	private String name;
	private int status;
	private String remark;
	private String activateDate;
	private String activateTime;
	private String createddate;
	private String modifieddate;
	private String startTime;
	private String startDate;
	private String endTime;
	private String endDate;
	private int activateStatus;
	private int isDelete;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emergency_syskey", referencedColumnName = "syskey")
	private Emergency emergency;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "condition_syskey", referencedColumnName = "syskey")
	private Condition condition;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "loc_emergency_syskey", referencedColumnName = "syskey")
//	private LocEmergency locEmergency;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
	@JoinTable(name = "ERC_Activate_Assembly", joinColumns = @JoinColumn(name = "emergency_activate_syskey"), inverseJoinColumns = @JoinColumn(name = "assembly_syskey"))
	private List<Assembly> assemblyList = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
	@JoinTable(name = "ERC_Activate_Route", joinColumns = @JoinColumn(name = "emergency_activate_syskey"), inverseJoinColumns = @JoinColumn(name = "route_syskey"))
	private List<Route> routeList = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
	@JoinTable(name = "ERC_Activate_Locemergency", joinColumns = @JoinColumn(name = "emergency_activate_syskey"), inverseJoinColumns = @JoinColumn(name = "Loc_Emergency_syskey"))
	private List<LocEmergency> locEmergencyList = new ArrayList<>();
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
	@JoinTable(name = "ERC_Activate_Main", joinColumns = @JoinColumn(name = "emergency_activate_syskey"), inverseJoinColumns = @JoinColumn(name = "main_building_syskey"))
	private List<MainBuilding> mainBuildingList = new ArrayList<>();

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

	public Emergency getEmergency() {
		return emergency;
	}

	public void setEmergency(Emergency emergency) {
		this.emergency = emergency;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public List<Assembly> getAssemblyList() {
		return assemblyList;
	}

	public void setAssemblyList(List<Assembly> assemblyList) {
		this.assemblyList = assemblyList;
	}

	public List<Route> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<Route> routeList) {
		this.routeList = routeList;
	}

	public List<LocEmergency> getLocEmergencyList() {
		return locEmergencyList;
	}

	public void setLocEmergencyList(List<LocEmergency> locEmergencyList) {
		this.locEmergencyList = locEmergencyList;
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

	public List<MainBuilding> getMainBuildingList() {
		return mainBuildingList;
	}

	public void setMainBuildingList(List<MainBuilding> mainBuildingList) {
		this.mainBuildingList = mainBuildingList;
	}

}
