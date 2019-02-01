package edu.uoregon.cs.calendar499;

import java.util.Calendar;

public class CalendarEvent {
	
	private String title;
	private Calendar timeStart;
	private Calendar timeEnd;
	private String note;
	
	/*constructor for event with no title*/
	public CalendarEvent(String title, Calendar timeStart, Calendar timeEnd) {
		this(title, timeStart, timeEnd, "", "");
	}
	
	
	public CalendarEvent(String title, Calendar timeStart, Calendar timeEnd, String note) {
		this.title = title;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.note = note;
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


	
	
}
