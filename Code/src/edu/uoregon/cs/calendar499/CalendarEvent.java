package edu.uoregon.cs.calendar499;

import java.util.Calendar;

public class CalendarEvent {
	
	public String title;
	public Calendar timeStart;
	public Calendar timeEnd;
	public String note;
	public String location;
	
	
	
	
	public CalendarEvent(String title, Calendar timeStart, Calendar timeEnd) {
		this(title, timeStart, timeEnd, "", "");
	}
	
	
	public CalendarEvent(String title, Calendar timeStart, Calendar timeEnd, String note, String location) {
		
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Calendar getTimeStart() {
		return timeStart;
	}


	public void setTimeStart(Calendar timeStart) {
		this.timeStart = timeStart;
	}


	public Calendar getTimeEnd() {
		return timeEnd;
	}


	public void setTimeEnd(Calendar timeEnd) {
		this.timeEnd = timeEnd;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}

	
	
}
