package com.emergency.rollcall.dto;

public class Message {
	private boolean state;
	private String code;
    private String message;

    // Constructors
    public Message() {}

    public Message(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    // Getters and Setters
    
    public String getCode() {
        return code;
    }

    public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
