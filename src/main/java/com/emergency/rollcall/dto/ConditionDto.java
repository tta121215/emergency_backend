package com.emergency.rollcall.dto;

public class ConditionDto {
	private long syskey;
	private String name;
	private String code;
	private int status;
	private int isDefault;
	private String createddate;
	private String modifieddate;
	private int isDelete;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
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

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
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
		return "ConditionDto [syskey=" + syskey + ", name=" + name + ", code=" + code + ", status=" + status
				+ ", isDefault=" + isDefault + ", createddate=" + createddate + ", modifieddate=" + modifieddate
				+ ", isDelete=" + isDelete + "]";
	}
}
