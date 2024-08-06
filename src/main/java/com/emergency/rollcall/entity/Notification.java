package com.emergency.rollcall.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ERC_Noti")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_seq_gen")
	@SequenceGenerator(name = "example_seq_gen", sequenceName = "example_seq", allocationSize = 1)
	private long syskey;
	private String notimode;
	private String notisubject;
	private String notimessage;
	private int status;
	private String createddate;
	private String modifieddate;

//	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
//	@JoinTable(name = "noti_modenoti", joinColumns = @JoinColumn(name = "noti_syskey"), inverseJoinColumns = @JoinColumn(name = "mode_noti_syskey"))
//	private List<ModeNoti> modeNotiList = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emergency_syskey", referencedColumnName = "syskey")
	private Emergency emergency;

//
//	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
//	@JoinTable(name = "noti_emergency", joinColumns = @JoinColumn(name = "noti_syskey"), inverseJoinColumns = @JoinColumn(name = "emergency_syskey"))
//	private List<Emergency> emergencyList = new ArrayList<>();

//	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
//	@JoinTable(name = "noti_route", joinColumns = @JoinColumn(name = "noti_syskey"), inverseJoinColumns = @JoinColumn(name = "route_syskey"))
//	private List<Route> routeList = new ArrayList<>();

	public long getSyskey() {
		return syskey;
	}

	public void setSyskey(long syskey) {
		this.syskey = syskey;
	}

	public String getNotimode() {
		return notimode;
	}

	public void setNotimode(String notimode) {
		this.notimode = notimode;
	}

	public String getNotisubject() {
		return notisubject;
	}

	public void setNotisubject(String notisubject) {
		this.notisubject = notisubject;
	}

	public String getNotimessage() {
		return notimessage;
	}

	public void setNotimessage(String notimessage) {
		this.notimessage = notimessage;
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

	public Emergency getEmergency() {
		return emergency;
	}

	public void setEmergency(Emergency emergency) {
		this.emergency = emergency;
	}

}
