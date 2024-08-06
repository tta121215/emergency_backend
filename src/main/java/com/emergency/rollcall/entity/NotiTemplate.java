package com.emergency.rollcall.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ERC_Noti_Template")
public class NotiTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_seq_gen")
	@SequenceGenerator(name = "example_seq_gen", sequenceName = "example_seq", allocationSize = 1)
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
	
}
