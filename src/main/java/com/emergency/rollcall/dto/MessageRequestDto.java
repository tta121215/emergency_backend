package com.emergency.rollcall.dto;

import java.util.List;

public class MessageRequestDto {
	
	private String subject;
    private List<String> content;
    private String link;
    private long emergancyId;
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public List<String> getContent() {
		return content;
	}
	public void setContent(List<String> content) {
		this.content = content;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public long getEmergancyId() {
		return emergancyId;
	}
	public void setEmergancyId(long emergancyId) {
		this.emergancyId = emergancyId;
	}
	
	
    
    
}
