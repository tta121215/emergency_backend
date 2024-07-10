package com.emergency.rollcall.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.poi.hpsf.Decimal;

@Entity
@Table(name = "ASSEMBLY001")
public class Assembly {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_seq_gen")
    @SequenceGenerator(name = "example_seq_gen", sequenceName = "example_seq", allocationSize = 1)
	private long syskey;
	private String name;
	private BigDecimal latitude;
	private BigDecimal longtiude;
	private String ipaddress;
	private int status;
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
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLongtiude() {
		return longtiude;
	}
	public void setLongtiude(BigDecimal longtiude) {
		this.longtiude = longtiude;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
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
	
	

}
