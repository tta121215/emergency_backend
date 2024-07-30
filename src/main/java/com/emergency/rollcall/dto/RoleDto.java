package com.emergency.rollcall.dto;

import java.util.List;

public class RoleDto {
	private long syskey;
	private String role;
	private List<MenuDto> menu;
	private int status;
	public long getSyskey() {
		return syskey;
	}

	public void setSyskey(long syskey) {
		this.syskey = syskey;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<MenuDto> getMenu() {
		return menu;
	}

	public void setMenu(List<MenuDto> menu) {
		this.menu = menu;
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

	private String createddate;
	private String modifieddate;

	

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
		return "RoleDto{" + "syskey=" + syskey + ", role='" + role + '\'' + ", menu='" + menu + '\'' + ", status='"
				+ status + '\'' + ", createddate=" + createddate + ", modifieddate=" + modifieddate + '}';
	}

}
