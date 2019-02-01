package edu.uoregon.cs.calendar499;

import java.util.ArrayList;
import java.util.Calendar;

public class Main {
	
	
	public Calendar cal = Calendar.getInstance();
	private GUI gui;
	private UserInput ui;
	public Cal globalCalendar;
	
	public Main() {
		globalCalendar = new Cal();
		Calendar today = CalMathAbs.ClearTime(Calendar.getInstance());
		ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>(); 
		globalCalendar.days.put(today, events);
		
		Calendar st = (Calendar)today.clone();
		Calendar et = (Calendar)today.clone();
		st.set(Calendar.HOUR, 8);
		et.set(Calendar.HOUR, 9);
		events.add(new CalendarEvent("E1", st, et));
		events.add(new CalendarEvent("E2", st, et));
		events.add(new CalendarEvent("E3", st, et));
		events.add(new CalendarEvent("E4", st, et));
		events.add(new CalendarEvent("E5", st, et));
		
		ui = new UserInput();
		gui = new GUI(this, ui,new View());
		ui.gui = gui;
	}
	
	public static void main(String[] args) {
		new Main();
		
	}

}
