package com.emergency.rollcall.dto;

public class LocationDto {

	private long id;
	private String name;

	public LocationDto(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "LocationDto [id=" + id + ", name=" + name + "]";
	}

}
