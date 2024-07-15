package com.emergency.rollcall.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ASSEMBLY")
public class Assembly {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_seq_gen")
	@SequenceGenerator(name = "example_seq_gen", sequenceName = "example_seq", allocationSize = 1)
	private long syskey;
	private String name;
	private String latitude;
	private String longtiude;
	private String ipaddress;
	private String status;
	private String accesstype;
	private String createddate;
	private String modifieddate;

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

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccesstype() {
		return accesstype;
	}

	public void setAccesstype(String accesstype) {
		this.accesstype = accesstype;
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

}
