package com.emergency.rollcall.dto;

public class HeaderDto {
	private String date;
	private String session;
	private String number;
	private long amount;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public HeaderDto(String date, String session, String number, long amount) {
		super();
		this.date = ddMMyyyFormat(date);
		this.session = session;
		this.number = number;
		this.amount = amount;
	}
	
	public String ddMMyyyFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null)
		//	l_Date = aDate.substring(6) + "/" + aDate.substring(4, 6) + "/" + aDate.substring(0, 4);
		l_Date = aDate.substring(0, 4) + "-" + aDate.substring(4, 6) + "-" + aDate.substring(6);

		return l_Date;
	}
	
	

}
