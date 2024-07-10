package com.emergency.rollcall.dto;

import java.math.BigDecimal;

public class AssemblyDto {
	private long id;
	private String name;
	private BigDecimal latitude;
	private BigDecimal longtiude;
	private String ipaddress;
	private int status;
	private String createddate;
	private String modifieddate;

	public AssemblyDto(long id, String name, BigDecimal latitude, BigDecimal longtiude, String ipaddress, int status,
			String createddate, String modifieddate) {
		super();
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longtiude = longtiude;
		this.ipaddress = ipaddress;
		this.status = status;
		this.createddate = ddMMyyyFormat(createddate);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String ddMMyyyFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null)
			// l_Date = aDate.substring(6) + "/" + aDate.substring(4, 6) + "/" +
			// aDate.substring(0, 4);
			l_Date = aDate.substring(0, 4) + "-" + aDate.substring(4, 6) + "-" + aDate.substring(6);

		return l_Date;
	}

}
