package edu.uoregon.cs.calendar499;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Cal {
	
	
	public HashMap<Calendar, ArrayList<CalendarEvent>> days = new HashMap<Calendar, ArrayList<CalendarEvent>>();
	 
	
	
	
	

	public ArrayList<CalendarEvent> grab(Calendar day){
		if(!days.containsKey(day)) {
			days.put(day, new ArrayList<CalendarEvent>());
		}
		return days.get(day);
	}
}
