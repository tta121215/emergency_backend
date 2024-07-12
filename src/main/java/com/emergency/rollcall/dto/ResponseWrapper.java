package com.emergency.rollcall.dto;



public class ResponseWrapper<T> {
	
	private Message message;
    private DataWrapper<T> data;
	// Constructors
   
    // Constructors
    public ResponseWrapper() {}

    public ResponseWrapper(Message message, DataWrapper<T> data) {
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public DataWrapper<T> getData() {
        return data;
    }

    public void setData(DataWrapper<T> data) {
        this.data = data;
    }

    public static class DataWrapper<T> {
        private T dto;

        // Constructors
        public DataWrapper() {}

        public DataWrapper(T dto) {
            this.dto = dto;
        }

        // Getters and Setters
        public T getDto() {
            return dto;
        }

        public void setDto(T dto) {
            this.dto = dto;
        }
    }
}
