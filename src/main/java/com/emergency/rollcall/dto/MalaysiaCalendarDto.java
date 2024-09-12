package com.emergency.rollcall.dto;


import java.time.LocalDate;
import java.util.List;

public class MalaysiaCalendarDto {
	private List<LocalDate> weekdays;
    private List<LocalDate> weekends;
    private List<LocalDate> publicHolidays;
    
    public MalaysiaCalendarDto(List<LocalDate> weekdays, List<LocalDate> weekends, List<LocalDate> publicHolidays) {
        this.weekdays = weekdays;
        this.weekends = weekends;
        this.publicHolidays = publicHolidays;
    }
    
	public List<LocalDate> getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(List<LocalDate> weekdays) {
		this.weekdays = weekdays;
	}
	public List<LocalDate> getWeekends() {
		return weekends;
	}
	public void setWeekends(List<LocalDate> weekends) {
		this.weekends = weekends;
	}
	public List<LocalDate> getPublicHolidays() {
		return publicHolidays;
	}
	public void setPublicHolidays(List<LocalDate> publicHolidays) {
		this.publicHolidays = publicHolidays;
	}
    
    
}
