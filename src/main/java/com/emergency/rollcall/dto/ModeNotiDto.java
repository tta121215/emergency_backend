package com.emergency.rollcall.dto;

public class ModeNotiDto {
	private long syskey;
	private String name;
	private String description;
	private int status;
	private String createddate;
	private String modifieddate;
	private String senderMail;
	private String passcode;
	private String hostServer;
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

	public String getSenderMail() {
		return senderMail;
	}

	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}

	public String getHostServer() {
		return hostServer;
	}

	public void setHostServer(String hostServer) {
		this.hostServer = hostServer;
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
		return "ModeNotiDto{" + "syskey=" + syskey + ", name='" + name + '\'' + ", description='" + description + '\''
				+ ", status='" + status + '\'' + ", createddate=" + createddate + ", modifieddate=" + modifieddate
				+ ", isDelete=" + isDelete + '}';
	}

}
