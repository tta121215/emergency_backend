package com.emergency.rollcall.dto;

public class EmailResponse {
	
	private String status;
    private String message;

    // Constructors
    public EmailResponse() {}

    public EmailResponse(String code, String message) {
        this.status = code;
        this.message = message;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    // Getters and Setters
    
   

   
}
