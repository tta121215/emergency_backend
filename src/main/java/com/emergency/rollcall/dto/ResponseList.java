package com.emergency.rollcall.dto;

import java.util.List;

public class ResponseList<T> {
	private Message message;
    private List<T> data;

    // Getters and Setters
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

    

}
