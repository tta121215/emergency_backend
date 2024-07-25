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
@Table(name = "emergency_activate")
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emergency_syskey", referencedColumnName = "syskey")	
	private Emergency emergency;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "condition_syskey", referencedColumnName = "syskey")	
	private Condition condition;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loc_emergency_syskey", referencedColumnName = "syskey")	
	private LocEmergency locEmergency;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notification_syskey", referencedColumnName = "syskey")	
	private Notification notification;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
	@JoinTable(name = "activate_assembly", joinColumns = @JoinColumn(name = "emergency_activate_syskey"), inverseJoinColumns = @JoinColumn(name = "assembly_syskey"))
	private List<Assembly> assemblyList = new ArrayList<>();
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
	@JoinTable(name = "activate_route", joinColumns = @JoinColumn(name = "emergency_activate_syskey"), inverseJoinColumns = @JoinColumn(name = "route_syskey"))
	private List<Route> routeList = new ArrayList<>();

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

	public LocEmergency getLocEmergency() {
		return locEmergency;
	}

	public void setLocEmergency(LocEmergency locEmergency) {
		this.locEmergency = locEmergency;
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

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

}
