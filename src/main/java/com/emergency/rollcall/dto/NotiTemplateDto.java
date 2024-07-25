package com.emergency.rollcall.dto;

public class NotiTemplateDto {
	private long syskey;
	private String noti_mode;
	private String noti_subject;
	private String noti_content;
	private int status;
	private String createddate;
	private String modifieddate;

	public long getSyskey() {
		return syskey;
	}

	public void setSyskey(long syskey) {
		this.syskey = syskey;
	}

	public String getNoti_mode() {
		return noti_mode;
	}

	public void setNoti_mode(String noti_mode) {
		this.noti_mode = noti_mode;
	}

	public String getNoti_subject() {
		return noti_subject;
	}

	public void setNoti_subject(String noti_subject) {
		this.noti_subject = noti_subject;
	}

	public String getNoti_content() {
		return noti_content;
	}

	public void setNoti_content(String noti_content) {
		this.noti_content = noti_content;
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

	@Override
	public String toString() {
		return "NotiTemplateDto{" + "syskey=" + syskey + ", mode='" + noti_mode + '\'' + ", subject='" + noti_subject + '\''
				+ ", content='" + noti_content + '\'' + ", status='" + status + '\'' + ", createddate=" + createddate
				+ ", modifieddate=" + modifieddate + '}';
	}

}
