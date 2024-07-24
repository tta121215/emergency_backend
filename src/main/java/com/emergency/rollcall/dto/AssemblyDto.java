package com.emergency.rollcall.dto;

public class AssemblyDto {
	private long syskey;
	private String name;
	private String latitude;
	private String longtiude;
	private String ipaddress;
	private int status;
	private String createddate;
	private String modifieddate;
	private String accesstype;

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

	public String getAccesstype() {
		return accesstype;
	}

	public void setAccesstype(String accesstype) {
		this.accesstype = accesstype;
	}

	public String ddMMyyyFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null)
			// l_Date = aDate.substring(6) + "/" + aDate.substring(4, 6) + "/" +
			// aDate.substring(0, 4);
			l_Date = aDate.substring(0, 4) + "-" + aDate.substring(4, 6) + "-" + aDate.substring(6);

		return l_Date;
	}

	@Override
	public String toString() {
		return "AssemblyDto{" + "syskey=" + syskey + ", name='" + name + '\'' + ", latitude='" + latitude + '\''
				+ ", longtiude='" + longtiude + '\'' + ", ipaddress='" + ipaddress + '\'' + ", accesstype='"
				+ accesstype + '\'' + ", status='" + status + '\'' + ", createddate=" + createddate + ", modifieddate="
				+ modifieddate + '}';
	}

}
