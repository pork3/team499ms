package edu.uoregon.cs.calendar499;

import java.util.Calendar;

public class CalendarEvent implements Cloneable{
	
	private String title; // This stores the title for the event
	private Calendar timeStart; // This stores the start time (only hours, minutes and seconds are significant, don't trust D/M/Y)
	private Calendar timeEnd;// This stores the end time (only hours, minutes and seconds are significant, don't trust D/M/Y)
	private String note; // This stores a note for the event
	
	
	@Override
	public Object clone() {
		return new CalendarEvent(title, timeStart, timeEnd, note);
	}
	
	
	/*constructor for event with no notes*/
	public CalendarEvent(String title, Calendar timeStart, Calendar timeEnd) {
		this(title, timeStart, timeEnd, "");
	}
	
	// Constructor for event with all fields filled. Will not store jCalendar's directly, and will clone them instead.
	public CalendarEvent(String title, Calendar timeStart, Calendar timeEnd, String note) {
		this.title = title;
		this.timeStart = (Calendar)timeStart.clone();
		this.timeEnd = (Calendar)timeEnd.clone();
		this.note = note;
	}

	/*
	 * Below are public getters and setters for the variables above.
	 */
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
